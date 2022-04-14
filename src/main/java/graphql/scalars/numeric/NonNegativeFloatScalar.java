package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NonNegativeFloat}
 */
@Internal
public final class NonNegativeFloatScalar {

    private NonNegativeFloatScalar() {}

    public static final GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("NonNegativeFloat")
            .description("An Float scalar that must be greater than or equal to zero")
            .coercing(new FloatCoercing() {
                @Override
                protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                    if (d < 0.0d) {
                        throw exceptionMaker.apply("The value must be greater than or equal to zero");
                    }
                    return d;
                }
            })
            .build();
}
