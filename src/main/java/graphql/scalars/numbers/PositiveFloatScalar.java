package graphql.scalars.numbers;

import graphql.schema.GraphQLScalarType;

import java.util.function.Function;

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
