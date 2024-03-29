package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#PositiveFloat}
 */
@Internal
public final class PositiveFloatScalar {

    private PositiveFloatScalar() {}

    public static final GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("PositiveFloat")
            .description("An Float scalar that must be a positive value")
            .coercing(new FloatCoercing() {
                @Override
                protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                    if (d <= 0) {
                        throw exceptionMaker.apply("The value must be a positive value");
                    }
                    return d;
                }
            })
            .build();
}
