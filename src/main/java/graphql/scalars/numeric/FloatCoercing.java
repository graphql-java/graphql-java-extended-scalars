package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.util.function.Function;

import static graphql.Scalars.GraphQLFloat;

@Internal
abstract class FloatCoercing implements Coercing<Double, Double> {

    abstract protected Double check(Double d, Function<String, RuntimeException> exceptionMaker);

    @Override
    public Double serialize(Object input) throws CoercingSerializeException {
        Double d = (Double) GraphQLFloat.getCoercing().serialize(input);
        return check(d, CoercingSerializeException::new);
    }

    @Override
    public Double parseValue(Object input) throws CoercingParseValueException {
        Double d = (Double) GraphQLFloat.getCoercing().parseValue(input);
        return check(d, CoercingParseValueException::new);
    }

    @Override
    public Double parseLiteral(Object input) throws CoercingParseLiteralException {
        Double d = (Double) GraphQLFloat.getCoercing().parseLiteral(input);
        return check(d, CoercingParseLiteralException::new);
    }
}
