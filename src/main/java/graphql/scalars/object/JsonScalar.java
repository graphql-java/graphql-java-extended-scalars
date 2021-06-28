package graphql.scalars.object;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import static graphql.scalars.object.ObjectScalar.OBJECT_COERCING;

/**
 * A synonym class for {@link graphql.scalars.object.ObjectScalar}
 *
 * Access this via {@link graphql.scalars.ExtendedScalars#Json}
 */
@Internal
public class JsonScalar {

    public static GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("JSON")
            .description("A JSON scalar")
            .coercing(OBJECT_COERCING)
            .build();
}
