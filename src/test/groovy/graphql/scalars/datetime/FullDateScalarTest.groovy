package graphql.scalars.datetime

import spock.lang.Specification
import spock.lang.Unroll

import java.time.format.DateTimeFormatter

class FullDateScalarTest extends Specification {

    def coercing = FullDateScalar.RFC3339_DATE_COERCING

    @Unroll
    def "full date parsing"() {

        when:
        def result = coercing.parseValue(input)
        then:
        DateTimeFormatter.ISO_DATE.format(result) == expectedValue
        where:
        input        | expectedValue
        "1985-04-12" | "1985-04-12"
        "1996-12-19" | "1996-12-19"
    }

}
