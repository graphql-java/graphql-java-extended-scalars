package graphql.scalars.datetime

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocalT

class LocalTimeScalarTest extends Specification {

    def coercing = ExtendedScalars.LocalTime.getCoercing()

    @Unroll
    def "localtime parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input                | expectedValue
        "23:20:50.123456789" | mkLocalT("23:20:50.123456789")
        "16:39:57.000000000" | mkLocalT("16:39:57")
        "16:39:57.0"         | mkLocalT("16:39:57")
        "16:39:57"           | mkLocalT("16:39:57")
    }

    @Unroll
    def "localtime parseValue bad inputs"() {

        when:
        coercing.parseValue(input)
        then:
        thrown(expectedValue)
        where:
        input            | expectedValue
        "23:20:50.52Z"   | CoercingParseValueException
        "16:39:57-08:00" | CoercingParseValueException
        666             || CoercingParseValueException
    }

    def "localtime AST literal"() {

        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                                 | expectedValue
        new StringValue("23:20:50.123456789") | mkLocalT("23:20:50.123456789")
        new StringValue("16:39:57.000000000") | mkLocalT("16:39:57")
        new StringValue("16:39:57.0")         | mkLocalT("16:39:57")
        new StringValue("16:39:57")           | mkLocalT("16:39:57")
    }

    def "localtime serialisation"() {

        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                  | expectedValue
        "23:20:50.123456789"   | "23:20:50.123456789"
        "23:20:50"             | "23:20:50"
        mkLocalT("16:39:57")   | "16:39:57"
        mkLocalT("16:39:57.1") | "16:39:57.1"
    }

    def "datetime serialisation bad inputs"() {

        when:
        coercing.serialize(input)
        then:
        thrown(expectedValue)
        where:
        input            | expectedValue
        "23:20:50.52Z"   | CoercingSerializeException
        "16:39:57-08:00" | CoercingSerializeException
        666             || CoercingSerializeException
    }
}
