package graphql.scalars.java

import graphql.language.BooleanValue
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

class ScalarsBigIntegerTest extends AbstractScalarTest {

    @Unroll
    def "BigInteger parse literal #literal.value as #result"() {
        expect:
        ExtendedScalars.GraphQLBigInteger.getCoercing().parseLiteral(literal, variables, graphQLContext, locale) == result

        where:
        literal                                 | result
        new IntValue(12345678910 as BigInteger) | new BigInteger("12345678910")
        new StringValue("12345678911")          | new BigInteger("12345678911")
        new FloatValue(new BigDecimal("42"))    | new BigInteger("42")
    }

    @Unroll
    def "BigInteger returns null for invalid #literal"() {
        when:
        ExtendedScalars.GraphQLBigInteger.getCoercing().parseLiteral(literal, variables, graphQLContext, locale)
        then:
        thrown(CoercingParseLiteralException)

        where:
        literal                                 | _
        new BooleanValue(true)                  | _
        new StringValue("42.3")                 | _
        new FloatValue(new BigDecimal("12.12")) | _
        new StringValue("not a number")         | _
    }

    @Unroll
    def "BigInteger serialize #value into #result (#result.class)"() {
        expect:
        ExtendedScalars.GraphQLBigInteger.getCoercing().serialize(value, graphQLContext, locale) == result
        ExtendedScalars.GraphQLBigInteger.getCoercing().parseValue(value, graphQLContext, locale) == result

        where:
        value                 | result
        "42"                  | new BigInteger("42")
        Integer.valueOf(42)   | new BigInteger("42")
        "-1"                  | new BigInteger("-1")
        new BigInteger("42")  | new BigInteger("42")
        42.0d                 | new BigInteger("42")
        Byte.valueOf("42")    | new BigInteger("42")
        Short.valueOf("42")   | new BigInteger("42")
        1234567l              | new BigInteger("1234567")
        new AtomicInteger(42) | new BigInteger("42")
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLBigInteger.getCoercing().serialize(value, graphQLContext, locale)
        then:
        thrown(CoercingSerializeException)

        where:
        value                   | _
        ""                      | _
        "not a number "         | _
        new BigDecimal("12.12") | _
        "12.12"                 | _
        new Object()            | _
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLBigInteger.getCoercing().parseValue(value, graphQLContext, locale)
        then:
        thrown(CoercingParseValueException)

        where:
        value                   | _
        ""                      | _
        "not a number "         | _
        new BigDecimal("12.12") | _
        "12.12"                 | _
        new Object()            | _
    }

}
