package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NegativeFloat}
 */
@Internal
public final class NegativeFloatScalar {

    private NegativeFloatScalar() {}

    public static final GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("NegativeFloat")
            .description("An Float scalar that must be a negative value")
            .coercing(new FloatCoercing() {
                @Override
                protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                    if (d >= 0.0d) {
                        throw exceptionMaker.apply("The value must be a negative value");
                    }
                    return d;
                }
            })
            .build();

}
