package graphql.scalars.java


import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import java.util.concurrent.atomic.AtomicInteger

class ScalarsShortTest extends AbstractScalarTest {

    @Unroll
    def "Short parse literal #literal.value as #result"() {
        expect:
        ExtendedScalars.GraphQLShort.getCoercing().parseLiteral(literal, variables, graphQLContext, locale) == result

        where:
        literal                                     | result
        new IntValue(42 as BigInteger)              | 42
        new IntValue(Short.MAX_VALUE as BigInteger) | Short.MAX_VALUE
        new IntValue(Short.MIN_VALUE as BigInteger) | Short.MIN_VALUE

    }

    @Unroll
    def "Short returns null for invalid #literal"() {
        when:
        ExtendedScalars.GraphQLShort.getCoercing().parseLiteral(literal, variables, graphQLContext, locale)
        then:
        thrown(CoercingParseLiteralException)

        where:
        literal                                          | _
        new IntValue(12345678910 as BigInteger)          | _
        new StringValue("-1")                            | _
        new FloatValue(42.3)                             | _
        new IntValue(Short.MAX_VALUE + 1l as BigInteger) | null
        new IntValue(Short.MIN_VALUE - 1l as BigInteger) | null
        new StringValue("-1")                            | null
        new FloatValue(42.3)                             | null
    }

    @Unroll
    def "Short serialize #value into #result (#result.class)"() {
        expect:
        ExtendedScalars.GraphQLShort.getCoercing().serialize(value, graphQLContext, locale) == result
        ExtendedScalars.GraphQLShort.getCoercing().parseValue(value, graphQLContext, locale) == result

        where:
        value                  | result
        "42"                   | 42
        "42.0000"              | 42
        42.0000d               | 42
        Integer.valueOf(42)    | 42
        "-1"                   | -1
        BigInteger.valueOf(42) | 42
        new BigDecimal("42")   | 42
        42.0f                  | 42
        42.0d                  | 42
        Byte.valueOf("42")     | 42
        Short.valueOf("42")    | 42
        1234l                  | 1234
        new AtomicInteger(42)  | 42
        Short.MAX_VALUE        | Short.MAX_VALUE
        Short.MIN_VALUE        | Short.MIN_VALUE
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLShort.getCoercing().serialize(value, graphQLContext, locale)
        then:
        thrown(CoercingSerializeException)

        where:
        value                        | _
        ""                           | _
        "not a number "              | _
        "42.3"                       | _
        new Long(42345784398534785l) | _
        new Double(42.3)             | _
        new Float(42.3)              | _
        Short.MAX_VALUE + 1l         | _
        Short.MIN_VALUE - 1l         | _
        new Object()                 | _

    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLShort.getCoercing().parseValue(value, graphQLContext, locale)
        then:
        thrown(CoercingParseValueException)

        where:
        value                        | _
        ""                           | _
        "not a number "              | _
        "42.3"                       | _
        new Long(42345784398534785l) | _
        new Double(42.3)             | _
        new Float(42.3)              | _
        Short.MAX_VALUE + 1l         | _
        Short.MIN_VALUE - 1l         | _
        new Object()                 | _

    }

}
