package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocalDT
import static graphql.scalars.util.TestKit.mkOffsetDT
import static graphql.scalars.util.TestKit.mkOffsetT
import static graphql.scalars.util.TestKit.mkStringValue
import static graphql.scalars.util.TestKit.mkZonedDT

class TimeScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.Time.getCoercing()

    @Unroll
    def "datetime parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "23:20:50.52Z"                  | mkOffsetT("23:20:50.52Z")
        "16:39:57-08:00"                | mkOffsetT("16:39:57-08:00")
        "12:00:27.87+00:20"             | mkOffsetT("12:00:27.87+00:20")
        mkOffsetDT(year: 1980, hour: 3) | mkOffsetT("03:10:09+10:00")
        mkZonedDT(year: 1980, hour: 3)  | mkOffsetT("03:10:09+10:00")
    }

    @Unroll
    def "datetime parseValue bad inputs"() {

        when:
        coercing.parseValue(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "1985-04-12"                   | CoercingParseValueException
        mkLocalDT(year: 1980, hour: 3) | CoercingParseValueException
        666                           || CoercingParseValueException
    }

    def "datetime AST literal"() {

        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        new StringValue("23:20:50.52Z") | mkOffsetT("23:20:50.52Z")
    }

    def "datetime serialisation"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "23:20:50.52Z"                  | "23:20:50.52Z"
        "16:39:57-08:00"                | "16:39:57-08:00"
        "12:00:27.87+00:20"             | "12:00:27.87+00:20"
        mkOffsetDT(year: 1980, hour: 3) | "03:10:09+10:00"
        mkZonedDT(year: 1980, hour: 3)  | "03:10:09+10:00"
    }

    def "datetime valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                           | expectedValue
        "23:20:50.52Z"                  | mkStringValue("23:20:50.52Z")
        "16:39:57-08:00"                | mkStringValue("16:39:57-08:00")
        "12:00:27.87+00:20"             | mkStringValue("12:00:27.87+00:20")
        mkOffsetDT(year: 1980, hour: 3) | mkStringValue("03:10:09+10:00")
        mkZonedDT(year: 1980, hour: 3)  | mkStringValue("03:10:09+10:00")
    }

    def "datetime serialisation bad inputs"() {

        when:
        coercing.serialize(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "1985-04-12"                   | CoercingSerializeException
        mkLocalDT(year: 1980, hour: 3) | CoercingSerializeException
        666                           || CoercingSerializeException
    }
}
