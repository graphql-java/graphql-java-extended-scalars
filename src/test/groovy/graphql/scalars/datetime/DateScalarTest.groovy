package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocalDate
import static graphql.scalars.util.TestKit.mkOffsetDT
import static graphql.scalars.util.TestKit.mkStringValue
import static graphql.scalars.util.TestKit.mkZonedDT

class DateScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.Date.getCoercing()

    @Unroll
    def "full date parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "1937-01-01"                    | mkLocalDate("1937-01-01")
        mkOffsetDT(year: 1980, hour: 3) | mkLocalDate("1980-08-08")
        mkZonedDT(year: 1980, hour: 3)  | mkLocalDate("1980-08-08")
    }

    @Unroll
    def "full date parseLiteral"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                         | expectedValue
        new StringValue("1937-01-01") | mkLocalDate("1937-01-01")
    }

    @Unroll
    def "full date serialize"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "1937-01-01"                    | "1937-01-01"
        mkOffsetDT(year: 1980, hour: 3) | "1980-08-08"
        mkZonedDT(year: 1980, hour: 3)  | "1980-08-08"
    }

    @Unroll
    def "full date valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                           | expectedValue
        "1937-01-01"                    | mkStringValue("1937-01-01")
        mkOffsetDT(year: 1980, hour: 3) | mkStringValue("1980-08-08")
        mkZonedDT(year: 1980, hour: 3)  | mkStringValue("1980-08-08")
    }

}
