package graphql.scalars.datetime

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

import static graphql.scalars.util.TestKit.mkIntValue
import static graphql.scalars.util.TestKit.mkLocalDT
import static graphql.scalars.util.TestKit.mkOffsetDT
import static graphql.scalars.util.TestKit.mkStringValue
import static graphql.scalars.util.TestKit.mkZonedDT

class SecondsSinceEpochScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.SecondsSinceEpoch.getCoercing()

    @Unroll
    def "secondsSinceEpoch parseValue"() {
        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result.toEpochSecond() == expectedValue
        where:
        input        | expectedValue
        "0"          | 0L
        "1"          | 1L
        "1609459200" | 1609459200L // 2021-01-01T00:00:00Z
        "1640995200" | 1640995200L // 2022-01-01T00:00:00Z
        0            | 0L
        1            | 1L
        1609459200   | 1609459200L // 2021-01-01T00:00:00Z
        1640995200   | 1640995200L // 2022-01-01T00:00:00Z
    }

    @Unroll
    def "secondsSinceEpoch valueToLiteral"() {
        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input        | expectedValue
        "0"          | mkIntValue(0)
        "1"          | mkIntValue(1)
        "1609459200" | mkIntValue(1609459200)
        "1640995200" | mkIntValue(1640995200)
        0            | mkIntValue(0)
        1            | mkIntValue(1)
        1609459200   | mkIntValue(1609459200)
        1640995200   | mkIntValue(1640995200)
        Instant.ofEpochSecond(1609459200) | mkIntValue(1609459200)
        ZonedDateTime.ofInstant(Instant.ofEpochSecond(1609459200), ZoneOffset.UTC) | mkIntValue(1609459200)
    }

    @Unroll
    def "secondsSinceEpoch parseValue bad inputs"() {
        when:
        coercing.parseValue(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "not a number"                 | CoercingParseValueException
        "123abc"                       | CoercingParseValueException
        "2022-01-01"                   | CoercingParseValueException
        "2022-01-01T00:00:00Z"         | CoercingParseValueException
        new Object()                   | CoercingParseValueException
    }

    def "secondsSinceEpoch AST literal"() {
        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result.toEpochSecond() == expectedValue
        where:
        input                      | expectedValue
        new StringValue("0")       | 0L
        new StringValue("1")       | 1L
        new StringValue("1609459200") | 1609459200L // 2021-01-01T00:00:00Z
        new IntValue(0)            | 0L
        new IntValue(1)            | 1L
        new IntValue(1609459200)   | 1609459200L // 2021-01-01T00:00:00Z
    }

    def "secondsSinceEpoch serialisation"() {
        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                                                                  | expectedValue
        Instant.ofEpochSecond(0)                                               | 0L
        Instant.ofEpochSecond(1)                                               | 1L
        Instant.ofEpochSecond(1609459200)                                      | 1609459200L
        LocalDateTime.ofInstant(Instant.ofEpochSecond(1609459200), ZoneOffset.UTC) | 1609459200L
        ZonedDateTime.ofInstant(Instant.ofEpochSecond(1609459200), ZoneOffset.UTC) | 1609459200L
        OffsetDateTime.ofInstant(Instant.ofEpochSecond(1609459200), ZoneOffset.UTC) | 1609459200L
    }

    def "secondsSinceEpoch serialisation bad inputs"() {
        when:
        coercing.serialize(input, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                          | expectedValue
        "not a temporal"               | CoercingSerializeException
        new Object()                   | CoercingSerializeException
    }

    @Unroll
    def "secondsSinceEpoch parseLiteral bad inputs"() {
        when:
        coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        thrown(expectedValue)
        where:
        input                                | expectedValue
        mkStringValue("not a number")        | CoercingParseLiteralException
        mkStringValue("123abc")              | CoercingParseLiteralException
        mkStringValue("2022-01-01")          | CoercingParseLiteralException
        mkStringValue("2022-01-01T00:00:00Z")| CoercingParseLiteralException
    }
}
