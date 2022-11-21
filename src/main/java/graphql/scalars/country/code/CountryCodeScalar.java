package graphql.scalars.country.code;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.*;

import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#CountryCode}
 */
@Internal
public class CountryCodeScalar {

    public static final GraphQLScalarType INSTANCE;

    static {
        Coercing<CountryCode, String> coercing = new Coercing<CountryCode, String>() {

            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                CountryCode countryCode = parseCountryCode(input, CoercingParseValueException::new);
                return countryCode.name();
            }

            @Override
            public CountryCode parseValue(Object input) throws CoercingParseValueException {
                return parseCountryCode(input, CoercingParseValueException::new);
            }

            @Override
            public CountryCode parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException("Expected AST type 'StringValue' but was '" + typeName(input) + "'.");
                }
                String stringValue = ((StringValue) input).getValue();
                return parseCountryCode(stringValue, CoercingParseLiteralException::new);

            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                String s = serialize(input);
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
