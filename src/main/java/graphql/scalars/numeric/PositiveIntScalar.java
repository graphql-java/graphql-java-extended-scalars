package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#PositiveInt}
 */
@Internal
public class PositiveIntScalar extends GraphQLScalarType {
    public PositiveIntScalar() {
        super("PositiveInt", "An Int scalar that must be a positive value", new IntCoercing() {
            @Override
            protected Integer check(Integer i, Function<String, RuntimeException> exceptionMaker) {
                if (!(i > 0)) {
                    throw exceptionMaker.apply("The value must be a positive integer");
                }
                return i;
            }
        });
    }
}
