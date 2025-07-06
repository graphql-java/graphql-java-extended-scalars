package graphql.scalars.util

import graphql.GraphQLContext
import graphql.execution.CoercedVariables
import spock.lang.Specification

/**
 * Base class for scalar test
 */
abstract class AbstractScalarTest extends Specification {

    GraphQLContext graphQLContext
    Locale locale
    CoercedVariables variables

    void setup() {
        graphQLContext = GraphQLContext.newContext().build()
        locale = Locale.getDefault()
        variables = CoercedVariables.emptyVariables()
    }

}
