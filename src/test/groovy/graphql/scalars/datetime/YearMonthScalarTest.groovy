package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import spock.lang.Specification
import spock.lang.Unroll

import java.time.YearMonth

import static graphql.scalars.util.TestKit.mkStringValue

class YearMonthScalarTest extends Specification {

    def coercing = ExtendedScalars.YearMonth.getCoercing()

    @Unroll
    def "yearMonth parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "1937-01"                       | YearMonth.of(1937, 1)
    }

    @Unroll
    def "yearMonth parseLiteral"() {

        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                         | expectedValue
        new StringValue("1937-01")    | YearMonth.of(1937, 1)
    }

    @Unroll
    def "yearMonth serialize"() {

        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "1937-01"                       | "1937-01"
    }

    @Unroll
    def "yearMonth valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                           | expectedValue
        "1937-01"                       | mkStringValue("1937-01")
    }

}
