package graphql.scalars.color.hex

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.CoercingParseValueException
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkColor

class HexColorCodeScalarTest extends Specification {

    def coercing = ExtendedScalars.HexColorCode.getCoercing()

    @Unroll
    def "invoke parseValue for hexCode"() {
        when:
        def result = coercing.parseValue(input)
        then:
        result.equals(expectedValue)
        where:
        input       | expectedValue
        "#ff0000"   | mkColor(0xff, 0, 0)
        "#123"      | mkColor(0x11, 0x22, 0x33)
        "#11223344" | mkColor(0x11, 0x22, 0x33, 0x44)
        "#1234"     | mkColor(0x11, 0x22, 0x33, 0x44)
    }

    @Unroll
    def "invoke parseLiteral for hexCode"() {
        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                        | expectedValue
        new StringValue("#ff0000")   | mkColor(0xff, 0, 0)
        new StringValue("#123")      | mkColor(0x11, 0x22, 0x33)
        new StringValue("#11223344") | mkColor(0x11, 0x22, 0x33, 0x44)
        new StringValue("#1234")     | mkColor(0x11, 0x22, 0x33, 0x44)
    }

    @Unroll
    def "invoke serialize with hexCode"() {
        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "#ff0000"                       | "#ff0000"
        "#123"                          | "#112233"
        "#11223344"                     | "#11223344"
        "#1234"                         | "#11223344"
        mkColor(0x21, 0x23, 0x33)       | "#212333"
        mkColor(0x21, 0x23, 0x33, 0x44) | "#21233344"
    }

    @Unroll
    def "invoke valueToLiteral with hexCode"() {
        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input                           | expectedValue
        "#ff0000"                       | new StringValue("#ff0000")
        "#123"                          | new StringValue("#112233")
        "#11223344"                     | new StringValue("#11223344")
        "#1234"                         | new StringValue("#11223344")
        mkColor(0x21, 0x23, 0x33)       | new StringValue("#212333")
        mkColor(0x21, 0x23, 0x33, 0x44) | new StringValue("#21233344")
    }

    @Unroll
    def "parseValue throws exception for invalid input #input"() {
        when:
        def result = coercing.parseValue(input)
        then:
        thrown(CoercingParseValueException)
        where:
        input                | _
        "ff000"              | _
        ""                   | _
        "not a hex code"     | _
        "42.3"               | _
        Double.valueOf(42.3) | _
        Float.valueOf(42.3)  | _
        new Object()         | _
    }

}