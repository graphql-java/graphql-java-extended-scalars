package graphql.scalars.numeric;

import graphql.Internal;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.util.function.Function;

import static graphql.Scalars.GraphQLInt;

@Internal
abstract class IntCoercing implements Coercing<Integer, Integer> {

    abstract protected Integer check(Integer i, Function<String, RuntimeException> exceptionMaker);

    @Override
    public Integer serialize(Object input) throws CoercingSerializeException {
        Integer i = (Integer) GraphQLInt.getCoercing().serialize(input);
        return check(i, CoercingSerializeException::new);
    }

    @Override
    public Integer parseValue(Object input) throws CoercingParseValueException {
        Integer i = (Integer) GraphQLInt.getCoercing().parseValue(input);
        return check(i, CoercingParseValueException::new);
    }

    @Override
    public Integer parseLiteral(Object input) throws CoercingParseLiteralException {
        Integer i = (Integer) GraphQLInt.getCoercing().parseLiteral(input);
        return check(i, CoercingParseLiteralException::new);
    }
}
