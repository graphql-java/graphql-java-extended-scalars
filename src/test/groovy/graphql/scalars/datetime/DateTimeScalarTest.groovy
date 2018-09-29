package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocalDT
import static graphql.scalars.util.TestKit.mkOffsetDT
import static graphql.scalars.util.TestKit.mkZonedDT

class DateTimeScalarTest extends Specification {

    def coercing = new DateTimeScalar().getCoercing()

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
        mkOffsetDT(year: 1980, hour: 3) | mkOffsetDT("1980-08-08T03:10:09+10:00")
        mkZonedDT(year: 1980, hour: 3)  | mkOffsetDT("1980-08-08T03:10:09+10:00")
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
        "1985-04-12T23:20:50.52Z"       | "1985-04-12T23:20:50.52Z"
        "1996-12-19T16:39:57-08:00"     | "1996-12-19T16:39:57-08:00"
        "1937-01-01T12:00:27.87+00:20"  | "1937-01-01T12:00:27.87+00:20"
        mkOffsetDT(year: 1980, hour: 3) | "1980-08-08T03:10:09+10:00"
        mkZonedDT(year: 1980, hour: 3)  | "1980-08-08T03:10:09+10:00"
    }

    def "datetime serialisation bad inputs"() {

        when:
        coercing.serialize(input)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "1985-04-12"                   | CoercingSerializeException
        mkLocalDT(year: 1980, hour: 3) | CoercingSerializeException
        666                           || CoercingSerializeException
    }

}
