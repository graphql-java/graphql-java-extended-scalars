package graphql.scalars.numeric;

import graphql.GraphQLContext;
import graphql.Internal;
import graphql.execution.CoercedVariables;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.util.Locale;
import java.util.function.Function;

import static graphql.Scalars.GraphQLFloat;

@Internal
abstract class FloatCoercing implements Coercing<Double, Double> {

    protected abstract Double check(Double d, Function<String, RuntimeException> exceptionMaker);

    @Override
    public Double serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
        Double d = (Double) GraphQLFloat.getCoercing().serialize(input, graphQLContext, locale);
        return check(d, CoercingSerializeException::new);
    }

    @Override
    public Double parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
        Double d = (Double) GraphQLFloat.getCoercing().parseValue(input, graphQLContext, locale);
        return check(d, CoercingParseValueException::new);
    }

    @Override
    public Double parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
        Double d = (Double) GraphQLFloat.getCoercing().parseLiteral(input, variables, graphQLContext, locale);
        return check(d, CoercingParseLiteralException::new);
    }

    @Override
    public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
        return GraphQLFloat.getCoercing().valueToLiteral(input, graphQLContext, locale);
    }

}
