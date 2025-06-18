package graphql.scalars.currency;

import graphql.GraphQLContext;
import graphql.Internal;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.Currency;
import java.util.Locale;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#Currency}
 */
@Internal
public class CurrencyScalar {

    public static final GraphQLScalarType INSTANCE;

    static {
        Coercing<Currency, String> coercing = new Coercing<>() {
            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                Currency currency = parseCurrency(input, CoercingSerializeException::new);
                return currency.getCurrencyCode();
            }

            @Override
            public Currency parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                return parseCurrency(input, CoercingParseValueException::new);
            }


            @Override
            public Currency parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + typeName(input) + "'.");
                }
                String stringValue = ((StringValue) input).getValue();
                return parseCurrency(stringValue, CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String serializedInput = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(serializedInput).build();
            }


            private Currency parseCurrency(Object input, Function<String, RuntimeException> exceptionMaker) {
                final Currency result;
                if (input instanceof Currency) {
                    result = (Currency) input;
                } else if (input instanceof String) {
                    try {
                        result = Currency.getInstance((String) input);
                    } catch (NullPointerException | IllegalArgumentException ex) {
                        throw exceptionMaker.apply("Invalid ISO 4217 value : '" + input + "'. because of : '" + ex.getMessage() + "'");
                    }
                } else {
                    throw exceptionMaker.apply("Expected a 'String' or 'Currency' but was '" + typeName(input) + "'.");
                }
                return result;
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("Currency")
                .description("An ISO-4217 compliant Currency Scalar")
                .coercing(coercing).build();
    }
}