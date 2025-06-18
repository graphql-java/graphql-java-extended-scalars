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

class ScalarsByteTest extends AbstractScalarTest {

    @Unroll
    def "Byte parse literal #literal.value as #result"() {
        expect:
        ExtendedScalars.GraphQLByte.getCoercing().parseLiteral(literal, variables, graphQLContext, locale) == result

        where:
        literal                                    | result
        new IntValue(42 as BigInteger)             | 42
        new IntValue(Byte.MAX_VALUE as BigInteger) | Byte.MAX_VALUE
        new IntValue(Byte.MIN_VALUE as BigInteger) | Byte.MIN_VALUE

    }

    @Unroll
    def "Byte returns null for invalid #literal"() {
        when:
        ExtendedScalars.GraphQLByte.getCoercing().parseLiteral(literal, variables, graphQLContext, locale)
        then:
        thrown(CoercingParseLiteralException)

        where:
        literal                                         | _
        new IntValue(12345678910 as BigInteger)         | _
        new StringValue("-1")                           | _
        new FloatValue(42.3)                            | _
        new IntValue(Byte.MAX_VALUE + 1l as BigInteger) | _
        new IntValue(Byte.MIN_VALUE - 1l as BigInteger) | _
        new StringValue("-1")                           | _
        new FloatValue(42.3)                            | _

    }

    @Unroll
    def "Byte serialize #value into #result (#result.class)"() {
        expect:
        ExtendedScalars.GraphQLByte.getCoercing().serialize(value, graphQLContext, locale) == result
        ExtendedScalars.GraphQLByte.getCoercing().parseValue(value, graphQLContext, locale) == result

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
        123l                   | 123
        new AtomicInteger(42)  | 42
        Byte.MAX_VALUE         | Byte.MAX_VALUE
        Byte.MIN_VALUE         | Byte.MIN_VALUE
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLByte.getCoercing().serialize(value, graphQLContext, locale)
        then:
        thrown(CoercingSerializeException)

        where:
        value                            | _
        ""                               | _
        "not a number "                  | _
        "42.3"                           | _
        Long.valueOf(42345784398534785l) | _
        Double.valueOf(42.3)             | _
        Float.valueOf(42.3)              | _
        Byte.MAX_VALUE + 1l              | _
        Byte.MIN_VALUE - 1l              | _
        new Object()                     | _

    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLByte.getCoercing().parseValue(value, graphQLContext, locale)
        then:
        thrown(CoercingParseValueException)

        where:
        value                            | _
        ""                               | _
        "not a number "                  | _
        "42.3"                           | _
        Long.valueOf(42345784398534785l) | _
        Double.valueOf(42.3)             | _
        Float.valueOf(42.3)              | _
        Byte.MAX_VALUE + 1l              | _
        Byte.MIN_VALUE - 1l              | _
        new Object()                     | _

    }

}
