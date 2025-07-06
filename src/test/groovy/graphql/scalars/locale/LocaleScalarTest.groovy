package graphql.scalars.locale

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocale
import static graphql.scalars.util.TestKit.mkStringValue

class LocaleScalarTest extends AbstractScalarTest {

    def coercing = ExtendedScalars.Locale.getCoercing()

    @Unroll
    def "full locale parseValue"() {

        when:
        def result = coercing.parseValue(input, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input          | expectedValue
        "en"           | mkLocale("en")
        "ro-RO"        | mkLocale("ro-RO")
        "zh-hakka"     | mkLocale("zh-hakka")
        mkLocale("en") | mkLocale("en")
    }

    @Unroll
    def "full Locale parseLiteral"() {
        when:
        def result = coercing.parseLiteral(input, variables, graphQLContext, locale)
        then:
        result == expectedValue
        where:
        input                    | expectedValue
        new StringValue("ro-RO") | mkLocale("ro-RO")
    }

    @Unroll
    def "full Locale serialization"() {
        when:
        def result = coercing.serialize(input, graphQLContext, locale)
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
        def result = coercing.valueToLiteral(input, graphQLContext, locale)
        then:
        result.isEqualTo(expectedValue)
        where:
        input             | expectedValue
        "ro-RO"           | mkStringValue("ro-RO")
        mkLocale("ro-RO") | mkStringValue("ro-RO")
        mkLocale("en")    | mkStringValue("en")
    }

}
