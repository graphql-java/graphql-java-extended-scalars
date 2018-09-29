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
import spock.lang.Specification
import spock.lang.Unroll

class ObjectScalarTest extends Specification {

    def variables = [
            "varRef1": "value1"
    ]

    def coercing = new ObjectScalar().getCoercing()

    @Unroll
    def "test AST parsing"() {

        when:
        def result = coercing.parseLiteral(input, variables)
        then:
        result == expectedResult
        where:
        input                | expectedResult
        mkStringValue("s")   | "s"
        mkFloatValue("99.9") | new BigDecimal("99.9")
        mkIntValue(666)      | 666
        mkBooleanValue(true) | true
        mkEnumValue("enum")  | "enum"
        mkNullValue()        | null
        mkVarRef("varRef1")  | "value1"
        mkArrayValue([
                mkStringValue("s"), mkIntValue(666)
        ])                   | ["s", 666]
    }

    @Unroll
    def "test AST object parsing"() {

        when:
        def result = coercing.parseLiteral(input, variables)
        then:
        result == expectedResult
        where:
        input | expectedResult
        mkObjectValue([
                fld1: mkStringValue("s"),
                fld2: mkIntValue(99),
                fld3: mkObjectValue([
                        childFld1: mkStringValue("child1"),
                        childFl2 : mkVarRef("varRef1")
                ])
        ])    | [fld1: "s", fld2: 99, fld3: [childFld1: "child1", childFl2: "value1"]]
    }

    @Unroll
    def "test serialize is always in and out"() {
        when:
        def result = coercing.serialize(input)
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
        def result = coercing.parseValue(input)
        then:
        result == expectedResult
        where:
        input  | expectedResult
        666    | 666
        "same" | "same"
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
        new IntValue(val)
    }

    FloatValue mkFloatValue(String val) {
        new FloatValue(new BigDecimal(val))
    }

    StringValue mkStringValue(String val) {
        new StringValue(val)
    }
}
