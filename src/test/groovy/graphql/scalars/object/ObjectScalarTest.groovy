package graphql.scalars.object

import graphql.language.ArrayValue
import graphql.language.BooleanValue
import graphql.language.EnumValue
import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.NullValue
import graphql.language.ObjectField
import graphql.language.ObjectValue
import graphql.language.StringValue
import graphql.language.Value
import graphql.language.VariableReference
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import spock.lang.Unroll

class ObjectScalarTest extends AbstractScalarTest {


    def coercing = ExtendedScalars.Object.getCoercing()

    @Override
    void setup() {
        variables = [
                "varRef1": "value1"
        ]

    }

    @Unroll
    def "test AST parsing"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input                                       | expectedResult
        mkStringValue("s")                          | "s"
        mkFloatValue("99.9")                        | new BigDecimal("99.9")
        mkIntValue(666)                             | 666
        mkBooleanValue(true)                        | true
        mkEnumValue("enum")                         | "enum"
        mkVarRef("varRef1")                         | "value1"
        mkArrayValue([
                mkStringValue("s"), mkIntValue(666)
        ] as List<Value>)                           | ["s", 666]
    }

    @Unroll
    def "test AST object parsing"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input                                               | expectedResult
        mkObjectValue([
                fld1: mkStringValue("s"),
                fld2: mkIntValue(99),
                fld3: mkObjectValue([
                        childFld1: mkStringValue("child1"),
                        childFl2 : mkVarRef("varRef1")
                ] as Map<String, Value>)
        ] as Map<String, Value>)                            | [fld1: "s", fld2: 99, fld3: [childFld1: "child1", childFl2: "value1"]]

        mkObjectValue([
                field1: mkNullValue()
        ] as Map<String, Value>)                            | [field1: null] // Nested NullValue inside ObjectValue
    }

    @Unroll
    def "test serialize is always in and out"() {
        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input  | expectedResult
        666    | 666
        "same" | "same"
    }

    @Unroll
    def "test parseValue is always in and out"() {
        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input  | expectedResult
        666    | 666
        "same" | "same"
    }

    @Unroll
    def "test valueToLiteral #input"() {
        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedResult)
        where:
        input                 | expectedResult
        "cba"                 | mkStringValue("cba")
        666                   | mkIntValue(666)
        666.99                | mkFloatValue("666.99")
        true                  | mkBooleanValue(true)
        null                  | mkNullValue()

        [l1: [l2: [l3: "x"]]] | mkObjectValue(
                [l1: mkObjectValue(
                        [l2: mkObjectValue(
                                [l3: mkStringValue("x")]
                        )]
                )]
        )
        ["x", 1, true]        | mkArrayValue([
                mkStringValue("x"),
                mkIntValue(1),
                mkBooleanValue(true)] as List<Value>)
    }


    ObjectValue mkObjectValue(Map<String, Value> fields) {
        def list = []
        for (String key : fields.keySet()) {
            list.add(new ObjectField(key, fields.get(key)))
        }
        new ObjectValue(list)
    }

    VariableReference mkVarRef(String name) {
        new VariableReference(name)
    }

    ArrayValue mkArrayValue(List<Value> values) {
        return new ArrayValue(values)
    }

    NullValue mkNullValue() {
        return NullValue.newNullValue().build()
    }

    EnumValue mkEnumValue(String val) {
        new EnumValue(val)
    }

    BooleanValue mkBooleanValue(boolean val) {
        return new BooleanValue(val)
    }

    IntValue mkIntValue(int val) {
        new IntValue(BigInteger.valueOf(val))
    }

    FloatValue mkFloatValue(String val) {
        new FloatValue(new BigDecimal(val))
    }

    StringValue mkStringValue(String val) {
        new StringValue(val)
    }
}
