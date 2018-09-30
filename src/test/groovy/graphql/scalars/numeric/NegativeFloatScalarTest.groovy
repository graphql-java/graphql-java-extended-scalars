package graphql.scalars.numeric

import graphql.language.StringValue
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.assertValueOrException
import static graphql.scalars.util.TestKit.mkFloatValue
import static graphql.scalars.util.TestKit.mkIntValue

class NegativeFloatScalarTest extends Specification {
    def coercing = new NegativeFloatScalar().getCoercing()

    @Unroll
    def "serialize"() {
        def result
        when:
        try {
            result = coercing.serialize(input)
        } catch (Exception e) {
            result = e
        }
        then:
        assertValueOrException(result, expectedResult)
        where:
        input || expectedResult
        "NaN" || CoercingSerializeException
        1     || CoercingSerializeException
        0     || CoercingSerializeException
        66.6  || CoercingSerializeException

        -666  || -666
        -66.6 || -66.6
    }

    @Unroll
    def "parseValue"() {
        def result
        when:
        try {
            result = coercing.parseValue(input)
        } catch (Exception e) {
            result = e
        }
        then:
        assertValueOrException(result, expectedResult)
        where:
        input || expectedResult
        "NaN" || CoercingParseValueException
        1     || CoercingParseValueException
        0     || CoercingParseValueException
        66.6  || CoercingParseValueException

        -666  || -666
        -66.6 || -66.6
    }

    @Unroll
    def "parseLiteral"() {
        def result
        when:
        try {
            result = coercing.parseLiteral(input)
        } catch (Exception e) {
            result = e
        }
        then:
        assertValueOrException(result, expectedResult)
        where:
        input                  || expectedResult
        new StringValue("NaN") || CoercingParseLiteralException
        mkIntValue(1)          || CoercingParseLiteralException
        mkIntValue(0)          || CoercingParseLiteralException
        mkFloatValue(66.6)     || CoercingParseLiteralException

        mkIntValue(-666)       || -666
        mkFloatValue(-66.6)    || -66.6
    }

}
