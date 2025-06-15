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

import static graphql.Scalars.GraphQLInt;

@Internal
abstract class IntCoercing implements Coercing<Integer, Integer> {

    protected abstract Integer check(Integer i, Function<String, RuntimeException> exceptionMaker);

    @Override
    public Integer serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
        Integer i = (Integer) GraphQLInt.getCoercing().serialize(input, graphQLContext, locale);
        return check(i, CoercingSerializeException::new);
    }

    @Override
    public Integer parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
        Integer i = (Integer) GraphQLInt.getCoercing().parseValue(input, graphQLContext, locale);
        return check(i, CoercingParseValueException::new);
    }

    @Override
    public Integer parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
        Integer i = (Integer) GraphQLInt.getCoercing().parseLiteral(input, variables, graphQLContext, locale);
        return check(i, CoercingParseLiteralException::new);
    }

    @Override
    public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
        return GraphQLInt.getCoercing().valueToLiteral(input, graphQLContext, locale);
    }
}
