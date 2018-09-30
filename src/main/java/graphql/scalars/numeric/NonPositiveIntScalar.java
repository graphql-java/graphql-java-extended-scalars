package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NonPositiveInt}
 */
@Internal
public class NonPositiveIntScalar extends GraphQLScalarType {
    public NonPositiveIntScalar() {
        super("NonPositiveInt", "An Int scalar that must be less than or equal to zero", new IntCoercing() {
            @Override
            protected Integer check(Integer i, Function<String, RuntimeException> exceptionMaker) {
                if (!(i <= 0)) {
                    throw exceptionMaker.apply("The value must be less than or equal to zero");
                }
                return i;
            }
        });
    }
}
