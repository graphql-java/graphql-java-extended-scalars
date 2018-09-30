package graphql.scalars.url

import graphql.language.BooleanValue
import graphql.language.StringValue
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

class UrlScalarTest extends Specification {

    def coercing = new UrlScalar().getCoercing()

    @Unroll
    def "test serialize"() {

        when:
        def result = coercing.serialize(input)
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
    def "test serialize bad inputs"() {
        when:
        coercing.serialize(input)
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
        def result = coercing.parseValue(input)
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
        coercing.parseValue(input)
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
        def result = coercing.parseLiteral(input)
        then:
        result == expectedResult
        where:
        input                                           | expectedResult
        new StringValue("http://www.graphql-java.com/") | new URL("http://www.graphql-java.com/")
    }

    @Unroll
    def "test parseLiteral bad inputs"() {
        when:
        coercing.parseLiteral(input)
        then:
        thrown(exceptionClas)
        where:
        input                        | exceptionClas
        new BooleanValue(true)       | CoercingParseLiteralException
        new StringValue("not/a/url") | CoercingParseLiteralException
    }


}