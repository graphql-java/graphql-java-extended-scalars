package graphql.scalars.country.code;

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

import java.util.Locale;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#CountryCode}
 */
@Internal
public class CountryCodeScalar {

    public static final GraphQLScalarType INSTANCE;

    static {
        Coercing<CountryCode, String> coercing = new Coercing<>() {

            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                CountryCode countryCode = parseCountryCode(input, CoercingParseValueException::new);
                return countryCode.name();
            }

            @Override
            public CountryCode parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                return parseCountryCode(input, CoercingParseValueException::new);
            }

            @Override
            public CountryCode parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + typeName(input) + "'.");
                }
                String stringValue = ((StringValue) input).getValue();
                return parseCountryCode(stringValue, CoercingParseLiteralException::new);

            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String s = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(s).build();
            }

            private CountryCode parseCountryCode(Object input, Function<String, RuntimeException> exceptionMaker) {
                final CountryCode result;
                if (input instanceof String) {
                    try {
                        result = CountryCode.valueOf((String) input);
                    } catch (NullPointerException | IllegalArgumentException ex) {
                        throw exceptionMaker.apply("Invalid ISO 3166-1 alpha-2 value : '" + input + "'. because of : '" + ex.getMessage() + "'");
                    }
                } else if (input instanceof CountryCode) {
                    result = (CountryCode) input;
                } else {
                    throw exceptionMaker.apply("Expected a 'String' or 'CountryCode' but was '" + typeName(input) + "'.");
                }
                return result;
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("CountryCode")
                .description("The CountryCode scalar type as defined by ISO 3166-1 alpha-2.")
                .coercing(coercing).build();
    }
}
