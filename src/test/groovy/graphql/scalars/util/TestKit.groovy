package graphql.scalars.util

import graphql.language.FloatValue
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.scalars.country.code.CountryCode

import java.time.Duration
import java.awt.Color
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class TestKit {

    static Locale mkLocale(String s) {
        Locale.forLanguageTag(s)
    }

    static LocalDate mkLocalDate(String s) {
        LocalDate.parse(s)
    }

    static OffsetDateTime mkOffsetDT(String s) {
        OffsetDateTime.parse(s)
    }

    static OffsetTime mkOffsetT(String s) {
        OffsetTime.parse(s)
    }

    static LocalTime mkLocalT(String s) {
        LocalTime.parse(s)
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

    static Duration mkDuration(String s) {
        Duration.parse(s)
    }

    static Duration mkDuration(args) {
        Duration.of(args.amount, args.unit)
    }

    static Period mkPeriod(String s) {
        Period.parse(s)
    }

    static Period mkPeriod(args) {
        Period.of(args.years, args.months, args.days)
    }

    static assertValueOrException(result, expectedResult) {
        if (result instanceof Exception) {
            assert result.class == expectedResult, "was " + result + " but expected exception " + expectedResult
        } else {
            assert result == expectedResult, "was " + result + " but expected " + expectedResult
        }
        true
    }


    static IntValue mkIntValue(int i) {
        return new IntValue(new BigInteger(String.valueOf(i)))
    }

    static IntValue mkIntValue(String s) {
        return new IntValue(new BigInteger(String.valueOf(s)))
    }

    static FloatValue mkFloatValue(int i) {
        return new FloatValue(new BigDecimal(String.valueOf(i)))
    }

    static FloatValue mkFloatValue(String s) {
        return new FloatValue(new BigDecimal(String.valueOf(s)))
    }

    static StringValue mkStringValue(String s) {
        return new StringValue(s)
    }

    static FloatValue mkFloatValue(double d) {
        return new FloatValue(new BigDecimal(d))
    }

    static UUID mkUUIDValue(String s) {
        return UUID.fromString(s)
    }

    static Currency mkCurrency(String currency) {
        return Currency.getInstance(currency)
    }

    static CountryCode mkCountryCode(String countryCode) {
        return CountryCode.valueOf(countryCode)
    }

    static Color mkColor(int r, int g, int b, int a) {
        return new Color(r, g, b, a)
    }
    static Color mkColor(int r, int g, int b) {
        return new Color(r, g, b)
    }
}