package graphql.scalars.uri


import graphql.language.BooleanValue
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkStringValue

class UriScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.Uri.getCoercing()

    @Unroll
    def "test serialize"() {

        when:
        def result = coercing.serialize(input, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input                                   | expectedResult
        new URL("http://www.graphql-java.com/") | new URL("http://www.graphql-java.com/")
        new URI("http://www.graphql-java.com/") | new URL("http://www.graphql-java.com/")
        new File("/this/that")                  | new URL("file:/this/that")
        "http://www.graphql-java.com/"          | new URL("http://www.graphql-java.com/")
    }

    @Unroll
    def "test valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedResult)
        where:
        input                                   | expectedResult
        new URL("http://www.graphql-java.com/") | mkStringValue("http://www.graphql-java.com/")
        new URI("http://www.graphql-java.com/") | mkStringValue("http://www.graphql-java.com/")
        new File("/this/that")                  | mkStringValue("file:/this/that")
        "http://www.graphql-java.com/"          | mkStringValue("http://www.graphql-java.com/")
    }

    @Unroll
    def "test serialize bad inputs"() {
        when:
        coercing.serialize(input, graphQLContext, locale)
        then:
        thrown(exceptionClas)
        where:
        input       || exceptionClas
        666         || CoercingSerializeException
        "not/a/url" || CoercingSerializeException
    }

    @Unroll
    def "test parseValue"() {
        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input                                   | expectedResult
        new URL("http://www.graphql-java.com/") | new URL("http://www.graphql-java.com/")
        new URI("http://www.graphql-java.com/") | new URL("http://www.graphql-java.com/")
        new File("/this/that")                  | new URL("file:/this/that")
        "http://www.graphql-java.com/"          | new URL("http://www.graphql-java.com/")
    }

    @Unroll
    def "test parseValue bad inputs"() {
        when:
        coercing.parseValue(input, graphQLContext, locale)
        then:
        thrown(exceptionClas)
        where:
        input       || exceptionClas
        666         || CoercingParseValueException
        "not/a/url" || CoercingParseValueException
    }

    @Unroll
    def "test parseLiteral"() {
        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedResult
        where:
        input                                           | expectedResult
        new StringValue("http://www.graphql-java.com/") | new URL("http://www.graphql-java.com/")
    }

    @Unroll
    def "test parseLiteral bad inputs"() {
        when:
        coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        thrown(exceptionClas)
        where:
        input                        | exceptionClas
        new BooleanValue(true)       | CoercingParseLiteralException
        new StringValue("1:not/a/uri") | CoercingParseLiteralException
    }


}
