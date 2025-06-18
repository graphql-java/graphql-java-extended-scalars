package graphql.scalars.java

import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Shared
import spock.lang.Unroll

import java.util.concurrent.atomic.AtomicInteger

class ScalarsLongTest extends AbstractScalarTest {

    @Shared
    def tooBig = new BigInteger(Long.toString(Long.MAX_VALUE)).add(new BigInteger("1"))
    @Shared
    def tooSmall = new BigInteger(Long.toString(Long.MIN_VALUE)).subtract(new BigInteger("1"))

    @Unroll
    def "Long parse literal #literal.value as #result"() {
        expect:
        ExtendedScalars.GraphQLLong.getCoercing().parseLiteral(literal, variables, graphQLContext, locale) == result

        where:
        literal                                    | result
        new IntValue(42 as BigInteger)             | 42
        new IntValue(Long.MAX_VALUE as BigInteger) | Long.MAX_VALUE
        new IntValue(Long.MIN_VALUE as BigInteger) | Long.MIN_VALUE
        new StringValue("12345678910")             | 12345678910
        new StringValue("-1")                      | -1

    }

    @Unroll
    def "Long returns null for invalid #literal"() {
        when:
        ExtendedScalars.GraphQLLong.getCoercing().parseLiteral(literal, variables, graphQLContext, locale)
        then:
        thrown(CoercingParseLiteralException)

        where:
        literal                         | _
        new StringValue("not a number") | _
        new FloatValue(42.3)            | _
    }

    @Unroll
    def "Long serialize #value into #result (#result.class)"() {
        expect:
        ExtendedScalars.GraphQLLong.getCoercing().serialize(value, graphQLContext, locale) == result
        ExtendedScalars.GraphQLLong.getCoercing().parseValue(value, graphQLContext, locale) == result

        where:
        value                            | result
        "42"                             | 42
        "42.0000"                        | 42
        42.0000d                         | 42
        Integer.valueOf(42)              | 42
        "-1"                             | -1
        BigInteger.valueOf(42)           | 42
        new BigDecimal("42")             | 42
        42.0f                            | 42
        42.0d                            | 42
        Byte.valueOf("42")               | 42
        Short.valueOf("42")              | 42
        12345678910l                     | 12345678910l
        new AtomicInteger(42)            | 42
        Long.MAX_VALUE                   | Long.MAX_VALUE
        Long.MIN_VALUE                   | Long.MIN_VALUE
        Long.valueOf(42345784398534785l) | 42345784398534785l
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLLong.getCoercing().serialize(value, graphQLContext, locale)
        then:
        thrown(CoercingSerializeException)

        where:
        value                | _
        ""                   | _
        "not a number "      | _
        "42.3"               | _
        Double.valueOf(42.3) | _
        Float.valueOf(42.3)  | _
        tooBig               | _
        tooSmall             | _
        new Object()         | _
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLLong.getCoercing().parseValue(value, graphQLContext, locale)
        then:
        thrown(CoercingParseValueException)

        where:
        value                | _
        ""                   | _
        "not a number "      | _
        "42.3"               | _
        Double.valueOf(42.3) | _
        Float.valueOf(42.3)  | _
        tooBig               | _
        tooSmall             | _
        new Object()         | _
    }

}
