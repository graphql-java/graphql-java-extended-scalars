package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocalDT
import static graphql.scalars.util.TestKit.mkOffsetDT
import static graphql.scalars.util.TestKit.mkStringValue
import static graphql.scalars.util.TestKit.mkZonedDT

class DateTimeScalarTest extends Specification {

    def coercing = ExtendedScalars.DateTime.getCoercing()

    @Unroll
    def "datetime parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "1985-04-12T23:20:50.52Z"       | mkOffsetDT("1985-04-12T23:20:50.52Z")
        "1996-12-19T16:39:57-08:00"     | mkOffsetDT("1996-12-19T16:39:57-08:00")
        "1937-01-01T12:00:27.87+00:20"  | mkOffsetDT("1937-01-01T12:00:27.87+00:20")
        "2022-11-24T01:00:01.02+00:00"  | mkOffsetDT("2022-11-24T01:00:01.02+00:00")
        mkOffsetDT(year: 1980, hour: 3) | mkOffsetDT("1980-08-08T03:10:09+10:00")
        mkZonedDT(year: 1980, hour: 3)  | mkOffsetDT("1980-08-08T03:10:09+10:00")
    }

    @Unroll
    def "datetime valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                           | expectedValue
        "1985-04-12T23:20:50.52Z"       | mkStringValue("1985-04-12T23:20:50.520Z")
        "1996-12-19T16:39:57-08:00"     | mkStringValue("1996-12-19T16:39:57.000-08:00")
        "1937-01-01T12:00:27.87+00:20"  | mkStringValue("1937-01-01T12:00:27.870+00:20")
        "1937-01-01T12:00+00:20"        | mkStringValue("1937-01-01T12:00:00.000+00:20")
        "2022-11-24T01:00:01.02+00:00"  | mkStringValue("2022-11-24T01:00:01.020Z")
        mkOffsetDT(year: 1980, hour: 3) | mkStringValue("1980-08-08T03:10:09.000+10:00")
        mkZonedDT(year: 1980, hour: 3)  | mkStringValue("1980-08-08T03:10:09.000+10:00")
    }

    @Unroll
    def "datetime parseValue bad inputs"() {

        when:
        coercing.parseValue(input)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "1985-04-12"                   | CoercingParseValueException
        "2022-11-24T01:00:01.02-00:00" | CoercingParseValueException
        mkLocalDT(year: 1980, hour: 3) | CoercingParseValueException
        666                           || CoercingParseValueException
    }

    def "datetime AST literal"() {

        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                                      | expectedValue
        new StringValue("1985-04-12T23:20:50.52Z") | mkOffsetDT("1985-04-12T23:20:50.52Z")
    }

    def "datetime serialisation"() {

        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "1985-04-12T23:20:50.52Z"       | "1985-04-12T23:20:50.520Z"
        "1996-12-19T16:39:57-08:00"     | "1996-12-19T16:39:57.000-08:00"
        "1937-01-01T12:00:27.87+00:20"  | "1937-01-01T12:00:27.870+00:20"
        "2022-11-24T01:00:01.02+00:00"  | "2022-11-24T01:00:01.020Z"
        mkOffsetDT(year: 1980, hour: 3) | "1980-08-08T03:10:09.000+10:00"
        mkZonedDT(year: 1980, hour: 3)  | "1980-08-08T03:10:09.000+10:00"
    }

    def "datetime serialisation bad inputs"() {

        when:
        coercing.serialize(input)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "1985-04-12"                   | CoercingSerializeException
        "2022-11-24T01:00:01.02-00:00" | CoercingSerializeException
        mkLocalDT(year: 1980, hour: 3) | CoercingSerializeException
        666                           || CoercingSerializeException
    }

    @Unroll
    def "datetime parseLiteral bad inputs"() {

        when:
        coercing.parseLiteral(input)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "2022-11-24T01:00:01.02-00:00" | CoercingParseLiteralException
    }

}
