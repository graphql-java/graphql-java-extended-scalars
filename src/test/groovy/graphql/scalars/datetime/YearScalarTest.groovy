package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import spock.lang.Unroll

import java.time.Year

import static graphql.scalars.util.TestKit.mkStringValue

class YearScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.Year.getCoercing()

    @Unroll
    def "year parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input  | expectedValue
        "1937" | Year.of(1937)
    }

    @Unroll
    def "year parseLiteral"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                   | expectedValue
        new StringValue("1937") | Year.of(1937)
    }

    @Unroll
    def "year serialize"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input  | expectedValue
        "1937" | "1937"
    }

    @Unroll
    def "year valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input  | expectedValue
        "1937" | mkStringValue("1937")
    }

}
