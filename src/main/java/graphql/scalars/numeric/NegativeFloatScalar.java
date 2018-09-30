package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NegativeFloat}
 */
@Internal
public class NegativeFloatScalar extends GraphQLScalarType {
    public NegativeFloatScalar() {
        super("NegativeFloat", "An Float scalar that must be a negative value", new FloatCoercing() {
            @Override
            protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                if (!(d < 0)) {
                    throw exceptionMaker.apply("The value must be a negative value");
                }
                return d;
            }
        });
    }
}
