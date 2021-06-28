package graphql.scalars.locale

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocale
import static graphql.scalars.util.TestKit.mkStringValue

class LocaleScalarTest extends Specification {

    def coercing = ExtendedScalars.Locale.getCoercing()

    @Unroll
    def "full locale parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input      | expectedValue
        "en"       | mkLocale("en")
        "ro-RO"    | mkLocale("ro-RO")
        "zh-hakka" | mkLocale("zh-hakka")
    }

    @Unroll
    def "full Locale parseLiteral"() {
        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                    | expectedValue
        new StringValue("ro-RO") | mkLocale("ro-RO")
    }

    @Unroll
    def "full Locale serialization"() {
        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input             | expectedValue
        "ro-RO"           | "ro-RO"
        mkLocale("ro-RO") | "ro-RO"
        mkLocale("en")    | "en"
    }

    @Unroll
    def "full Locale valueToLiteral"() {
        when:
        def result = coercing.valueToLiteral(input)
        then:
        result.isEqualTo(expectedValue)
        where:
        input             | expectedValue
        "ro-RO"           | mkStringValue("ro-RO")
        mkLocale("ro-RO") | mkStringValue("ro-RO")
        mkLocale("en")    | mkStringValue("en")
    }

}
