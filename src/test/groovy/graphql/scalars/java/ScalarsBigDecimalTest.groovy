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

class ScalarsBigDecimalTest extends AbstractScalarTest {

    @Unroll
    def "BigDecimal parse literal #literal.value as #result"() {
        expect:
        ExtendedScalars.GraphQLBigDecimal.getCoercing().parseLiteral(literal, variables, graphQLContext, locale) == result

        where:
        literal                                 | result
        new IntValue(12345678910 as BigInteger) | new BigDecimal("12345678910")
        new StringValue("12345678911.12")       | new BigDecimal("12345678911.12")
        new FloatValue(new BigDecimal("42.42")) | new BigDecimal("42.42")

    }

    @Unroll
    def "BigDecimal returns null for invalid #literal"() {
        when:
        ExtendedScalars.GraphQLBigDecimal.getCoercing().parseLiteral(literal, variables, graphQLContext, locale)
        then:
        thrown(CoercingParseLiteralException)

        where:
        literal                         | _
        new BooleanValue(true)          | _
        new StringValue("not a number") | _
    }

    @Unroll
    def "BigDecimal serialize #value into #result (#result.class)"() {
        expect:
        ExtendedScalars.GraphQLBigDecimal.getCoercing().serialize(value, graphQLContext, locale) == result
        ExtendedScalars.GraphQLBigDecimal.getCoercing().parseValue(value, graphQLContext, locale) == result

        where:
        value                  | result
        "42"                   | new BigDecimal("42")
        "42.123"               | new BigDecimal("42.123")
        42.0000d               | new BigDecimal("42.000")
        Integer.valueOf(42)    | new BigDecimal("42")
        "-1"                   | new BigDecimal("-1")
        BigInteger.valueOf(42) | new BigDecimal("42")
        new BigDecimal("42")   | new BigDecimal("42")
        42.3f                  | new BigDecimal("42.3")
        42.0d                  | new BigDecimal("42")
        Byte.valueOf("42")     | new BigDecimal("42")
        Short.valueOf("42")    | new BigDecimal("42")
        1234567l               | new BigDecimal("1234567")
        new AtomicInteger(42)  | new BigDecimal("42")
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLBigDecimal.getCoercing().serialize(value, graphQLContext, locale)
        then:
        thrown(CoercingSerializeException)

        where:
        value           | _
        ""              | _
        "not a number " | _
        new Object()    | _
    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLBigDecimal.getCoercing().parseValue(value, graphQLContext, locale)
        then:
        thrown(CoercingParseValueException)

        where:
        value           | _
        ""              | _
        "not a number " | _
        new Object()    | _
    }

}
