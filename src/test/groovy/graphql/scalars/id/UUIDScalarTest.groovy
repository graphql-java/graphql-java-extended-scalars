package graphql.scalars.id

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkStringValue
import static graphql.scalars.util.TestKit.mkUUIDValue

class UUIDScalarTest extends Specification {

    def coercing = ExtendedScalars.UUID.getCoercing()

    @Unroll
    def "UUID parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input                                  | expectedValue
        "43f20307-603c-4ad1-83c6-6010d224fabf" | mkUUIDValue("43f20307-603c-4ad1-83c6-6010d224fabf")
        "787dbc2b-3ddb-4098-ad1d-63d026bac111" | mkUUIDValue("787dbc2b-3ddb-4098-ad1d-63d026bac111")
    }

    @Unroll
    def "UUID parseValue bad inputs"() {

        when:
        coercing.parseValue(input)
        then:
        thrown(expectedValue)
        where:
        input                       | expectedValue
        "a-string-that-is-not-uuid" | CoercingParseValueException
        100                         | CoercingParseValueException
        "1985-04-12"                | CoercingParseValueException
    }

    def "UUID AST literal"() {

        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                                                   | expectedValue
        new StringValue("6972117d-3963-4214-ab2c-fa973d7e996b") | mkUUIDValue("6972117d-3963-4214-ab2c-fa973d7e996b")
    }

    def "UUID AST literal bad inputs"() {

        when:
        coercing.parseLiteral(input)
        then:
        thrown(expectedValue)
        where:
        input                                        | expectedValue
        new StringValue("a-string-that-us-not-uuid") | CoercingParseLiteralException
    }

    def "UUID serialization"() {

        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                                               | expectedValue
        "42287d47-c5bd-45e4-b470-53e426d3d503"              | "42287d47-c5bd-45e4-b470-53e426d3d503"
        "423df0f3-cf05-4eb5-b708-ae2f4b4a052d"              | "423df0f3-cf05-4eb5-b708-ae2f4b4a052d"
        mkUUIDValue("6a90b1e6-20f3-43e5-a7ba-34db8010c071") | "6a90b1e6-20f3-43e5-a7ba-34db8010c071"
    }

    def "UUID serialization bad inputs"() {

        when:
        coercing.serialize(input)
        then:
        thrown(expectedValue)
        where:
        input        | expectedValue
        "1985-04-12" | CoercingSerializeException
        100          | CoercingSerializeException
    }

    @Unroll
    def "UUID valueToLiteral"() {

        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                                               | expectedValue
        "42287d47-c5bd-45e4-b470-53e426d3d503"              | mkStringValue("42287d47-c5bd-45e4-b470-53e426d3d503")
        "423df0f3-cf05-4eb5-b708-ae2f4b4a052d"              | mkStringValue("423df0f3-cf05-4eb5-b708-ae2f4b4a052d")
        mkUUIDValue("6a90b1e6-20f3-43e5-a7ba-34db8010c071") | mkStringValue("6a90b1e6-20f3-43e5-a7ba-34db8010c071")
    }
}
