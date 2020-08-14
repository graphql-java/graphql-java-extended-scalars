package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.ZonedDateTime

import static graphql.scalars.util.TestKit.mkOffsetDT

class OffsetDateTimeScalarTest extends Specification {

    @Unroll
    def "parse literal success for #input"() {

        when:
        def result = OffsetDateTimeScalar.coercing.parseLiteral(new StringValue(input))
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "2011-08-30T13:22:53.108Z"      | mkOffsetDT("2011-08-30T13:22:53.108Z")
        "2011-08-30t13:22:53.108Z"      | mkOffsetDT("2011-08-30T13:22:53.108Z")
        "2011-08-30T13:22:53.108z"      | mkOffsetDT("2011-08-30T13:22:53.108Z")
        "2011-08-30T13:22:53.108+00:00" | mkOffsetDT("2011-08-30T13:22:53.108+00:00")
        "2011-08-30T13:22:53.108-03:00" | mkOffsetDT("2011-08-30T13:22:53.108-03:00")
        "2011-08-30T13:22:53.108+03:00" | mkOffsetDT("2011-08-30T13:22:53.108+03:00")
    }

    @Unroll
    def "parse literal failure for #input #reason"() {
        when:
        OffsetDateTimeScalar.coercing.parseLiteral(new StringValue(input))
        then:
        thrown(CoercingParseLiteralException)
        where:

        input                              | reason
        '2011-08-30T13:22:53.108-03'       | "The minutes of the offset are missing."
        '2011-08-30T13:22:53.108912Z'      | "Too many digits for fractions of a second. Exactly three expected."
        '2011-08-30T24:22:53Z'             | "Fractions of a second are missing."
        '2011-08-30T13:22:53.108'          | "No offset provided."
        '2011-08-30'                       | "No time provided."
        '2011-08-30T13:22:53.108-00:00'    | "Negative offset ('-00:00') is not allowed"
//        '2011-08-30T13:22:53.108+03:30:15' | "Seconds are not allowed for the offset" Bug in Java 8, fixed in later versions
        '2011-08-30T24:22:53.108Z'         | "'24' is not allowed as hour of the time."
        '2010-02-30T21:22:53.108Z'         | '30th of February is not a valid date'
        '2010-02-11T21:22:53.108Z+25:11'   | "25 is not a valid hour for offset"
    }

    @Unroll
    def "result coercion (serialize) accepts OffsetDateTime for #input"() {
        when:
        def result = OffsetDateTimeScalar.coercing.serialize(input)
        then:
        result == expectedResult
        where:
        input                                       | expectedResult
        mkOffsetDT("2011-08-30T13:22:53.108Z")      | "2011-08-30T13:22:53.108Z"
        mkOffsetDT("2011-08-30T13:22:53.108+00:00") | "2011-08-30T13:22:53.108Z"
    }

    @Unroll
    def "result coercion (serialize) rejects #input because #reason"() {
        when:
        OffsetDateTimeScalar.coercing.serialize(input)
        then:
        thrown(CoercingSerializeException)
        where:
        input                                   | reason
        mkOffsetDT("2011-08-30T13:22:53.1234Z") | "too specific fractions of second"
        ZonedDateTime.now()                     | "zoned date time can't be converted without loss of information"
    }

    @Unroll
    def "value coercion successful for #input"() {
        when:
        def result = OffsetDateTimeScalar.coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input                                  | expectedValue
        "2011-08-30T13:22:53.108Z"             | mkOffsetDT("2011-08-30T13:22:53.108Z")
        mkOffsetDT("2011-08-30T13:22:53.108Z") | mkOffsetDT("2011-08-30T13:22:53.108Z")
    }

    @Unroll
    def "value coercion failed for #input because #reason"() {
        when:
        OffsetDateTimeScalar.coercing.parseValue(input)
        then:
        thrown(CoercingParseValueException)
        where:
        input                                   | reason
        mkOffsetDT("2011-08-30T13:22:53.1234Z") | "too specific fractions of second"
        ZonedDateTime.now()                     | "zoned date time can't be converted without loss of information"
    }


}
