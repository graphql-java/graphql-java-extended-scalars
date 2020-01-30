package graphql.scalars.java

import graphql.Scalars
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

class ScalarsCharTest extends Specification {

    @Unroll
    def "Char parse literal #literal.value as #result"() {
        expect:
        JavaPrimitives.GraphQLChar.getCoercing().parseLiteral(literal) == result

        where:
        literal              | result
        new StringValue("a") | 'a'
        new StringValue("b") | 'b'

    }

    @Unroll
    def "Short returns null for invalid #literal"() {
        when:
        JavaPrimitives.GraphQLChar.getCoercing().parseLiteral(literal)
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
        JavaPrimitives.GraphQLChar.getCoercing().serialize(value) == result
        JavaPrimitives.GraphQLChar.getCoercing().parseValue(value) == result

        where:
        value | result
        "a"   | 'a'
        'z'   | 'z'
    }

    @Unroll
    def "serialize throws exception for invalid input #value"() {
        when:
        JavaPrimitives.GraphQLChar.getCoercing().serialize(value)
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
        JavaPrimitives.GraphQLChar.getCoercing().parseValue(value)
        then:
        thrown(CoercingParseValueException)

        where:
        value        | _
        ""           | _
        "aa"         | _
        new Object() | _

    }

}
