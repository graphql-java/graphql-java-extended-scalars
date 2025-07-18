package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import java.time.Period
import java.time.temporal.ChronoUnit

import static graphql.scalars.util.TestKit.mkDuration
import static graphql.scalars.util.TestKit.mkIntValue
import static graphql.scalars.util.TestKit.mkStringValue

class AccurateDurationScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.AccurateDuration.getCoercing()

    @Unroll
    def "accurateduration parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                                              | expectedValue
        "PT1S"                                             | mkDuration("PT1S")
        "PT1.5S"                                           | mkDuration("PT1.5S")
        "P1DT2H3M4S"                                       | mkDuration("P1DT2H3M4S")
        "-P1DT2H3M4S"                                      | mkDuration("-P1DT2H3M4S")
        "P1DT-2H3M4S"                                      | mkDuration("P1DT-2H3M4S")
        mkDuration(amount: 123456, unit: ChronoUnit.HOURS) | mkDuration("PT123456H")
    }

    @Unroll
    def "accurateduration valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                                              | expectedValue
        "PT1S"                                             | mkStringValue("PT1S")
        "PT1.5S"                                           | mkStringValue("PT1.5S")
        "P1D"                                              | mkStringValue("PT24H")
        "P1DT2H3M4S"                                       | mkStringValue("PT26H3M4S")
        mkDuration("P1DT2H3M4S")                           | mkStringValue("PT26H3M4S")
        mkDuration("-P1DT2H3M4S")                          | mkStringValue("PT-26H-3M-4S")
        mkDuration(amount: 123456, unit: ChronoUnit.HOURS) | mkStringValue("PT123456H")
    }

    @Unroll
    def "accurateduration parseValue bad inputs"() {

        when:
        coercing.parseValue(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input              | expectedValue
        "P1M"              | CoercingParseValueException
        "P1MT2H"           | CoercingParseValueException
        "P2W"              | CoercingParseValueException
        "P3Y"              | CoercingParseValueException
        123                | CoercingParseValueException
        ""                 | CoercingParseValueException
        Period.of(1, 2, 3) | CoercingParseValueException
    }

    def "accurateduration AST literal"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                         | expectedValue
        new StringValue("P1DT2H3M4S") | mkDuration("P1DT2H3M4S")
    }

    def "accurateduration serialisation"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                                              | expectedValue
        "PT1S"                                             | "PT1S"
        "PT1.5S"                                           | "PT1.5S"
        "P1DT2H3M4S"                                       | "PT26H3M4S"
        "-P1DT2H3M4S"                                      | "PT-26H-3M-4S"
        "P1DT-2H3M4S"                                      | "PT22H3M4S"
        mkDuration("P1DT-2H3M4S")                          | "PT22H3M4S"
        mkDuration(amount: 123456, unit: ChronoUnit.HOURS) | "PT123456H"
    }

    def "accurateduration serialisation bad inputs"() {

        when:
        coercing.serialize(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input              | expectedValue
        "P1M"              | CoercingSerializeException
        "PT1.5M"           | CoercingSerializeException
        "P1MT2H"           | CoercingSerializeException
        "P2W"              | CoercingSerializeException
        "P3Y"              | CoercingSerializeException
        123                | CoercingSerializeException
        ""                 | CoercingSerializeException
        Period.of(1, 2, 3) | CoercingSerializeException
    }

    @Unroll
    def "accurateduration parseLiteral bad inputs"() {

        when:
        coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                   | expectedValue
        mkStringValue("P1M")    | CoercingParseLiteralException
        mkStringValue("PT1.5M") | CoercingParseLiteralException
        mkStringValue("P1MT2H") | CoercingParseLiteralException
        mkStringValue("P2W")    | CoercingParseLiteralException
        mkStringValue("P3Y")    | CoercingParseLiteralException
        mkIntValue(123)         | CoercingParseLiteralException
        mkStringValue("")       | CoercingParseLiteralException
    }
}
