package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NegativeInt}
 */
@Internal
public class NegativeIntScalar extends GraphQLScalarType {
    public NegativeIntScalar() {
        super("NegativeInt", "An Int scalar that must be a negative value", new IntCoercing() {
            @Override
            protected Integer check(Integer i, Function<String, RuntimeException> exceptionMaker) {
                if (!(i < 0)) {
                    throw exceptionMaker.apply("The value must be a negative integer");
                }
                return i;
            }
        });
    }
}
