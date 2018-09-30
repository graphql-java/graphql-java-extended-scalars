package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#PositiveFloat}
 */
@Internal
public class PositiveFloatScalar extends GraphQLScalarType {
    public PositiveFloatScalar() {
        super("PositiveFloat", "An Float scalar that must be a positive value", new FloatCoercing() {
            @Override
            protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                if (!(d > 0)) {
                    throw exceptionMaker.apply("The value must be a positive value");
                }
                return d;
            }
        });
    }
}
