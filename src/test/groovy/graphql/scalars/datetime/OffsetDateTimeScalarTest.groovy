package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.schema.CoercingParseLiteralException
import spock.lang.Specification
import spock.lang.Unroll

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
        '2011-08-30T13:22:53.108+03:30:15' | "Seconds are not allowed for the offset"
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


}
