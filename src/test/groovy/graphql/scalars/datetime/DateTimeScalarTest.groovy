package graphql.scalars.datetime

import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DateTimeScalarTest extends Specification {

    def coercing = DateTimeScalar.RFC3339_DATETIME_COERCING

    @Unroll
    def "datetime parsing"() {

        when:
        def result = coercing.parseValue(input)
        then:
        DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(result) == expectedValue
        where:
        input                          | expectedValue
        "1985-04-12T23:20:50.52Z"      | "1985-04-12T23:20:50.52Z"
        "1996-12-19T16:39:57-08:00"    | "1996-12-19T16:39:57-08:00"
        "1937-01-01T12:00:27.87+00:20" | "1937-01-01T12:00:27.87+00:20"
    }

    def "datetime serialisation"() {

        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        mkOffsetDT(year: 1980, hour: 3) | "1980-08-08T03:10:09+10:00"
        mkLocalDT(year: 1980, hour: 3)  | "1980-08-08T03:10:09+10:00"

    }

    OffsetDateTime mkOffsetDT(args) {
        OffsetDateTime.of(args.year ?: 1969, args.month ?: 8, args.day ?: 8, args.hour ?: 11, args.min ?: 10, args.secs ?: 9, args.nanos ?: 0, ZoneOffset.ofHours(10))
    }

    LocalDateTime mkLocalDT(args) {
        LocalDateTime.of(args.year ?: 1969, args.month ?: 8, args.day ?: 8, args.hour ?: 11, args.min ?: 10, args.secs ?: 9, args.nanos ?: 0)
    }
}
