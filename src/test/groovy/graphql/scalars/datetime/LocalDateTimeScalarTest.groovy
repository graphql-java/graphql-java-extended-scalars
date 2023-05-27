package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

import static graphql.scalars.util.TestKit.*

class LocalDateTimeScalarTest extends Specification {

  def coercing = ExtendedScalars.LocalDateTime.getCoercing()

  @Unroll
  def "localDatetime parseValue"() {

    when:
    def result = coercing.parseValue(input)
    then:
    result == expectedValue
    where:
    input                                      | expectedValue
    "1985-04-12 23:20:50"                      | mkLocalDT("1985-04-12T23:20:50")
    "1985-04-12 23:20:50"                      | mkLocalDT("1985-04-12T23:20:50.000")
    LocalDateTime.of(1985, 04, 12, 23, 20, 50) | mkLocalDT("1985-04-12T23:20:50.000")
    LocalDateTime.of(1985, 04, 12, 23, 20)     | mkLocalDT("1985-04-12T23:20:00")
  }

  @Unroll
  def "localDatetime parseLiteral"() {

    when:
    def result = coercing.parseLiteral(input)
    then:
    result == expectedValue
    where:
    input                                      | expectedValue
    "1985-04-12 23:20:50"                      | mkLocalDT("1985-04-12T23:20:50.00")
    "1996-12-19 16:39:57"                      | mkLocalDT("1996-12-19T16:39:57")
    new StringValue("1996-12-19 16:39:57")     | mkLocalDT("1996-12-19T16:39:57")
    LocalDateTime.of(1996, 12, 19, 16, 39, 57) | mkLocalDT("1996-12-19T16:39:57")
  }

  @Unroll
  def "localDatetime valueToLiteral"() {

    when:
    def result = coercing.valueToLiteral(input)
    then:
    result.isEqualTo(expectedValue)
    where:
    input                                      | expectedValue
    LocalDateTime.of(1996, 12, 19, 16, 39, 57) | new StringValue("1996-12-19 16:39:57")
  }

  @Unroll
  def "localDatetime parseValue bad inputs"() {

    when:
    coercing.parseValue(input)
    then:
    thrown(expectedValue)
    where:
    input                                 | expectedValue
    "1985-04-12"                          | CoercingParseValueException
    "2022-11-24T01:00:01.02-00:00"        | CoercingParseValueException
    mkOffsetDT("1985-04-12T23:20:50.52Z") | CoercingParseValueException
    666                                  || CoercingParseValueException
  }

  def "localDatetime serialisation"() {

    when:
    def result = coercing.serialize(input)
    then:
    result == expectedValue
    where:
    input                                      | expectedValue
    LocalDateTime.of(1996, 12, 19, 16, 39, 57) | "1996-12-19 16:39:57"
  }

  def "localDatetime serialisation bad inputs"() {

    when:
    coercing.serialize(input)
    then:
    thrown(expectedValue)
    where:
    input                          | expectedValue
    "1985-04-12"                   | CoercingSerializeException
    "2022-11-24T01:00:01.02-00:00" | CoercingSerializeException
    mkOffsetDT(year: 1980, hour: 3) | CoercingSerializeException
    666                           || CoercingSerializeException
  }

  @Unroll
  def "localDatetime parseLiteral bad inputs"() {

    when:
    coercing.parseLiteral(input)
    then:
    thrown(expectedValue)
    where:
    input                          | expectedValue
    "2022-11-24T01:00:01.02-00:00" | CoercingParseLiteralException
  }

}
