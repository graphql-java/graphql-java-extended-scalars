package graphql.scalars.datetime

import spock.lang.Specification
import spock.lang.Unroll

import java.time.format.DateTimeFormatter

class FullTimeScalarTest extends Specification {

    def coercing = FullTimeScalar.RFC3339_TIME_COERCING

    @Unroll
    def "full time parsing"() {

        when:
        def result = coercing.parseValue(input)
        then:
        DateTimeFormatter.ISO_OFFSET_TIME.format(result) == expectedValue
        where:
        input               | expectedValue
        "10:15:30Z"         | "10:15:30Z"
        "16:39:57-08:00"    | "16:39:57-08:00"
        "19:39:57.27-07:00" | "19:39:57.27-07:00"
        "12:00:27.87+00:20" | "12:00:27.87+00:20"
    }

}
