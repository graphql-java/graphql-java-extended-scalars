package graphql.scalars.numeric

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.assertValueOrException
import static graphql.scalars.util.TestKit.mkIntValue

class NonNegativeIntScalarTest extends AbstractScalarTest {
    def coercing = ExtendedScalars.NonNegativeInt.getCoercing()

    @Unroll
    def "serialize"() {
        def result
        when:
        try {
            result = coercing.serialize(input, graphQLContext, locale)
        } catch (Exception e) {
            result = e
        }
        then:
        assertValueOrException(result, expectedResult)
        where:
        input || expectedResult
        "NaN" || CoercingSerializeException
        -1    || CoercingSerializeException

        0     || 0
        666   || 666
    }

    @Unroll
    def "parseValue"() {
        def result
        when:
        try {
            result = coercing.parseValue(input, graphQLContext, locale)
        } catch (Exception e) {
            result = e
        }
        then:
        assertValueOrException(result, expectedResult)
        where:
        input || expectedResult
        "NaN" || CoercingParseValueException
        -1    || CoercingParseValueException

        0     || 0
        666   || 666
    }

    @Unroll
    def "parseLiteral"() {
        def result
        when:
        try {
            result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        } catch (Exception e) {
            result = e
        }
        then:
        assertValueOrException(result, expectedResult)
        where:
        input                  || expectedResult
        new StringValue("NaN") || CoercingParseLiteralException
        mkIntValue(-1)         || CoercingParseLiteralException
        mkIntValue(0)          || 0

        mkIntValue(666)        || 666
    }
}
