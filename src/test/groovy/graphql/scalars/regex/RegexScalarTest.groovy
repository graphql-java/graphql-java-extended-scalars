package graphql.scalars.regex

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import spock.lang.Unroll

import java.util.regex.Pattern

import static graphql.scalars.util.TestKit.mkStringValue

class RegexScalarTest extends AbstractScalarTest {

    GraphQLScalarType phoneNumberScalar = ExtendedScalars.newRegexScalar("phoneNumber")
            .addPattern(Pattern.compile("\\([0-9]*\\)[0-9]*"))
            .build()

    @Unroll
    def "basic regex parseValue"() {

        when:
        def result = phoneNumberScalar.getCoercing().parseValue(input, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input        || expectedResult
        "(02)998768" || "(02)998768"
    }

    @Unroll
    def "basic regex parseValue bad input"() {
        when:
        phoneNumberScalar.getCoercing().parseValue(input, graphQLContext, locale)
        then:
        thrown(expectedResult)
        where:
        input        || expectedResult
        "(02)abc123" || CoercingParseValueException
    }

    @Unroll
    def "basic regex parseLiteral"() {

        when:
        def result = phoneNumberScalar.getCoercing().parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input                         || expectedResult
        new StringValue("(02)998768") || "(02)998768"
    }

    @Unroll
    def "basic regex parseLiteral bad input"() {
        when:
        phoneNumberScalar.getCoercing().parseLiteral(input, variables, graphQLContext, locale)
        then:
        thrown(expectedResult)
        where:
        input                       || expectedResult
        mkStringValue("(02)abc123") || CoercingParseLiteralException
    }

    @Unroll
    def "basic regex serialize"() {

        when:
        def result = phoneNumberScalar.getCoercing().serialize(input, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input        || expectedResult
        "(02)998768" || "(02)998768"
    }

    @Unroll
    def "basic regex valueToLiteral"() {

        when:
        def result = phoneNumberScalar.getCoercing().valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedResult)
        where:
        input        || expectedResult
        "(02)998768" || mkStringValue("(02)998768")
    }

    @Unroll
    def "basic regex serialize bad input"() {
        when:
        phoneNumberScalar.getCoercing().serialize(input, graphQLContext, locale)
        then:
        thrown(expectedResult)
        where:
        input                       || expectedResult
        mkStringValue("(02)abc123") || CoercingSerializeException
    }

}
