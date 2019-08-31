package graphql.scalars.locale

import graphql.language.StringValue
import spock.lang.Specification
import spock.lang.Unroll

import static graphql.scalars.util.TestKit.mkLocalDate
import static graphql.scalars.util.TestKit.mkLocalDate
import static graphql.scalars.util.TestKit.mkLocalDate
import static graphql.scalars.util.TestKit.mkLocale
import static graphql.scalars.util.TestKit.mkOffsetDT
import static graphql.scalars.util.TestKit.mkZonedDT

class LocaleScalarTest extends Specification {

    def coercing = new LocaleScalar().getCoercing()

    @Unroll
    def "full locale parseValue"() {

        when:
        def result = coercing.parseValue(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "en"                            | mkLocale("en")
        "ro-RO"                         | mkLocale("ro-RO")
        "zh-hakka"                      | mkLocale("zh-hakka")
    }

    @Unroll
    def "full Locale parseLiteral"() {
        when:
        def result = coercing.parseLiteral(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        new StringValue("ro-RO")        | mkLocale("ro-RO")
    }

    @Unroll
    def "full Locale serialization"() {
        when:
        def result = coercing.serialize(input)
        then:
        result == expectedValue
        where:
        input                           | expectedValue
        "ro-RO"                         | "ro-RO"
        mkLocale("ro-RO")               | "ro-RO"
        mkLocale("en")                  | "en"
    }

}
