package graphql.scalars.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TestKit {
    static LocalDate mkLocalDate(String s) {
        LocalDate.parse(s)
    }

    static OffsetDateTime mkOffsetDT(String s) {
        OffsetDateTime.parse(s)
    }

    static OffsetTime mkOffsetT(String s) {
        OffsetTime.parse(s)
    }

    static OffsetDateTime mkOffsetDT(args) {
        OffsetDateTime.of(args.year ?: 1969, args.month ?: 8, args.day ?: 8, args.hour ?: 11,
                args.min ?: 10, args.secs ?: 9, args.nanos ?: 0, ZoneOffset.ofHours(10))
    }

    static LocalDateTime mkLocalDT(args) {
        LocalDateTime.of(args.year ?: 1969, args.month ?: 8, args.day ?: 8, args.hour ?: 11,
                args.min ?: 10, args.secs ?: 9, args.nanos ?: 0)
    }

    static ZonedDateTime mkZonedDT(args) {
        ZonedDateTime.of(args.year ?: 1969, args.month ?: 8, args.day ?: 8, args.hour ?: 11,
                args.min ?: 10, args.secs ?: 9, args.nanos ?: 0, ZoneId.ofOffset("", ZoneOffset.ofHours(10)))
    }

}
