package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import spock.lang.Unroll

import java.time.YearMonth

import static graphql.scalars.util.TestKit.mkStringValue

class YearMonthScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.YearMonth.getCoercing()

    @Unroll
    def "yearMonth parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input     | expectedValue
        "1937-01" | YearMonth.of(1937, 1)
    }

    @Unroll
    def "yearMonth parseLiteral"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                      | expectedValue
        new StringValue("1937-01") | YearMonth.of(1937, 1)
    }

    @Unroll
    def "yearMonth serialize"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input     | expectedValue
        "1937-01" | "1937-01"
    }

    @Unroll
    def "yearMonth valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input     | expectedValue
        "1937-01" | mkStringValue("1937-01")
    }

}
