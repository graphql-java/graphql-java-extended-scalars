package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NonPositiveInt}
 */
@Internal
public class NonPositiveFloatScalar extends GraphQLScalarType {
    public NonPositiveFloatScalar() {
        super("NonPositiveFloat", "An Float scalar that must be less than or equal to zero", new FloatCoercing() {
            @Override
            protected Double check(Double d, Function<String, RuntimeException> exceptionMaker) {
                if (!(d <= 0)) {
                    throw exceptionMaker.apply("The value must be less than or equal to zero");
                }
                return d;
            }
        });
    }
}
