package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NonNegativeFloat}
 */
@Internal
public class NonNegativeFloatScalar extends GraphQLScalarType {
    public NonNegativeFloatScalar() {
        super("NonNegativeFloat", "An Float scalar that must be greater than or equal to zero", new FloatCoercing() {
            @Override
            protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                if (!(d >= 0)) {
                    throw exceptionMaker.apply("The value must be greater than or equal to zero");
                }
                return d;
            }
        });
    }
}
