package graphql.scalars.alias

import graphql.Scalars
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import spock.lang.Specification

class AliasedScalarTest extends Specification {

    AliasedScalar socialMediaLink = ExtendedScalars.newAliasedScalar("SocialMediaLink")
            .aliasedScalar(Scalars.GraphQLString)
            .build()

    def "basic wrapping"() {

        when:
        def result = socialMediaLink.coercing.serialize("ABC")
        then:
        result == "ABC"

        when:
        result = socialMediaLink.coercing.parseValue("ABC")
        then:
        result == "ABC"

        when:
        result = socialMediaLink.coercing.parseLiteral(new StringValue("ABC"))
        then:
        result == "ABC"

        when:
        result = socialMediaLink.coercing.parseLiteral(new StringValue("ABC"), [:])
        then:
        result == "ABC"

    }
}
