package graphql.scalars.java

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Unroll

class ScalarsCharTest extends AbstractScalarTest {

    @Unroll
    def "Char parse literal #literal.value as #result"() {
        expect:
        ExtendedScalars.GraphQLChar.getCoercing().parseLiteral(literal, variables, graphQLContext, locale) == result

        where:
        literal              | result
        new StringValue("a") | 'a'
        new StringValue("b") | 'b'

    }

    @Unroll
    def "Short returns null for invalid #literal"() {
        when:
        ExtendedScalars.GraphQLChar.getCoercing().parseLiteral(literal, variables, graphQLContext, locale)
        then:
        thrown(CoercingParseLiteralException)

        where:
        literal                        | _
        new StringValue("aa")          | _
        new IntValue(12 as BigInteger) | _
    }

    @Unroll
    def "Short serialize #value into #result (#result.class)"() {
        expect:
        ExtendedScalars.GraphQLChar.getCoercing().serialize(value, graphQLContext, locale) == result
        ExtendedScalars.GraphQLChar.getCoercing().parseValue(value, graphQLContext, locale) == result

        where:
        value | result
        "a"   | 'a'
        'z'   | 'z'
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLChar.getCoercing().serialize(value, graphQLContext, locale)
        then:
        thrown(CoercingSerializeException)

        where:
        value        | _
        ""           | _
        "aa"         | _
        new Object() | _

    }

    @Unroll
    def "parseValue throws exception for invalid input #value"() {
        when:
        ExtendedScalars.GraphQLChar.getCoercing().parseValue(value, graphQLContext, locale)
        then:
        thrown(CoercingParseValueException)

        where:
        value        | _
        ""           | _
        "aa"         | _
        new Object() | _

    }

}
