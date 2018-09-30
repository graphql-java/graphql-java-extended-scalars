package graphql.scalars.regex

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Pattern

class RegexScalarTest extends Specification {

    RegexScalar phoneNumberScalar = ExtendedScalars.newRegexScalar("phoneNumber")
            .addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*"))
            .build()

    @Unroll
    def "basic regex parseValue"() {

        when:
        def result = phoneNumberScalar.getCoercing().parseValue(input)
        then:
        result == expectedResult
        where:
        input        || expectedResult
        "(02)998768" || "(02)998768"
    }

    @Unroll
    def "basic regex parseValue bad input"() {
        when:
        phoneNumberScalar.getCoercing().parseValue(input)
        then:
        thrown(expectedResult)
        where:
        input        || expectedResult
        "(02)abc123" || CoercingParseValueException
    }

    @Unroll
    def "basic regex parseLiteral"() {

        when:
        def result = phoneNumberScalar.getCoercing().parseLiteral(input)
        then:
        result == expectedResult
        where:
        input                         || expectedResult
        new StringValue("(02)998768") || "(02)998768"
    }

    @Unroll
    def "basic regex parseLiteral bad input"() {
        when:
        phoneNumberScalar.getCoercing().parseLiteral(input)
        then:
        thrown(expectedResult)
        where:
        input        || expectedResult
        "(02)abc123" || CoercingParseLiteralException
    }

    @Unroll
    def "basic regex serialize"() {

        when:
        def result = phoneNumberScalar.getCoercing().serialize(input)
        then:
        result == expectedResult
        where:
        input        || expectedResult
        "(02)998768" || "(02)998768"
    }

    @Unroll
    def "basic regex serialize bad input"() {
        when:
        phoneNumberScalar.getCoercing().serialize(input)
        then:
        thrown(expectedResult)
        where:
        input        || expectedResult
        "(02)abc123" || CoercingSerializeException
    }

}
