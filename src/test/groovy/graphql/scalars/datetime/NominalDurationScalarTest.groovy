package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import java.time.Duration
import java.time.temporal.ChronoUnit

import static graphql.scalars.util.TestKit.mkIntValue
import static graphql.scalars.util.TestKit.mkPeriod
import static graphql.scalars.util.TestKit.mkStringValue

class NominalDurationScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.NominalDuration.getCoercing()

    @Unroll
    def "nominalduration parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                                  | expectedValue
        "P1D"                                  | mkPeriod("P1D")
        "P1W"                                  | mkPeriod("P7D")
        "P1Y2M3D"                              | mkPeriod("P1Y2M3D")
        "-P1Y2M3D"                             | mkPeriod("-P1Y2M3D")
        "P1Y-2M3D"                             | mkPeriod("P1Y-2M3D")
        mkPeriod(years: 1, months: 2, days: 3) | mkPeriod("P1Y2M3D")
    }

    @Unroll
    def "nominalduration valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                                  | expectedValue
        "P1D"                                  | mkStringValue("P1D")
        "P1W"                                  | mkStringValue("P7D")
        "P1Y2M3D"                              | mkStringValue("P1Y2M3D")
        "-P1Y2M3D"                             | mkStringValue("P-1Y-2M-3D")
        "P1Y-2M3D"                             | mkStringValue("P1Y-2M3D")
        mkPeriod("P1Y2M3D")                    | mkStringValue("P1Y2M3D")
        mkPeriod(years: 1, months: 2, days: 3) | mkStringValue("P1Y2M3D")
    }

    @Unroll
    def "nominalduration parseValue bad inputs"() {

        when:
        coercing.parseValue(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                               | expectedValue
        "P1.5M"                             | CoercingParseValueException
        "P1MT2H"                            | CoercingParseValueException
        "PT1S"                              | CoercingParseValueException
        123                                 | CoercingParseValueException
        ""                                  | CoercingParseValueException
        Duration.of(30, ChronoUnit.MINUTES) | CoercingParseValueException
    }

    def "nominalduration AST literal"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                      | expectedValue
        new StringValue("P1Y2M3D") | mkPeriod("P1Y2M3D")
    }

    def "nominalduration serialisation"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                                  | expectedValue
        "P1D"                                  | "P1D"
        "P1W"                                  | "P7D"
        "P1Y2M3D"                              | "P1Y2M3D"
        "-P1Y2M3D"                             | "P-1Y-2M-3D"
        "P1Y-2M3D"                             | "P1Y-2M3D"
        mkPeriod(years: 1, months: 2, days: 3) | "P1Y2M3D"
    }

    def "nominalduration serialisation bad inputs"() {

        when:
        coercing.serialize(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                              | expectedValue
        "PT1M"                             | CoercingSerializeException
        "P1.5M"                            | CoercingSerializeException
        "P1MT2H"                           | CoercingSerializeException
        "PY"                               | CoercingSerializeException
        123                                | CoercingSerializeException
        ""                                 | CoercingSerializeException
        Duration.of(1, ChronoUnit.MINUTES) | CoercingSerializeException
    }

    @Unroll
    def "nominalduration parseLiteral bad inputs"() {

        when:
        coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                   | expectedValue
        mkStringValue("PT1M")   | CoercingParseLiteralException
        mkStringValue("P1.5M")  | CoercingParseLiteralException
        mkStringValue("P1MT2H") | CoercingParseLiteralException
        mkStringValue("PY")     | CoercingParseLiteralException
        mkIntValue(123)         | CoercingParseLiteralException
        mkStringValue("")       | CoercingParseLiteralException
    }
}
