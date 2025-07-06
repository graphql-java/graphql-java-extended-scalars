package graphql.scalars.alias

import graphql.Scalars
import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.scalars.util.AbstractScalarTest
import graphql.schema.GraphQLScalarType

class AliasedScalarTest extends AbstractScalarTest {

    GraphQLScalarType socialMediaLink = ExtendedScalars.newAliasedScalar("SocialMediaLink")
            .aliasedScalar(Scalars.GraphQLString)
            .build()

    def "basic wrapping"() {

        when:
        def result = socialMediaLink.coercing.serialize("ABC", graphQLContext, locale)
        then:
        result == "ABC"

        when:
        result = socialMediaLink.coercing.parseValue("ABC", graphQLContext, locale)
        then:
        result == "ABC"

        when:
        result = socialMediaLink.coercing.parseLiteral(new StringValue("ABC"), variables, graphQLContext, locale)
        then:
        result == "ABC"

    }
}
