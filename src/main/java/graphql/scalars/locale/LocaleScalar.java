package graphql.scalars.locale;

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

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#Locale}
 */
@Internal
public final class LocaleScalar {

    private LocaleScalar() {
    }

    public static final GraphQLScalarType INSTANCE;

    static {
        Coercing<Locale, String> coercing = new Coercing<>() {

            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                if (input instanceof String) {
                    try {
                        return Locale.forLanguageTag((String) input).toLanguageTag();
                    } catch (Exception e) {
                        throw new CoercingSerializeException(
                                "Expected a valid language tag string but was but was " + typeName(input));
                    }
                }
                if (input instanceof Locale) {
                    return ((Locale) input).toLanguageTag();
                } else {
                    throw new CoercingSerializeException(
                            "Expected a 'java.util.Locale' object but was " + typeName(input));
                }
            }

            @Override
            public Locale parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                if (input instanceof String) {
                    try {
                        return Locale.forLanguageTag(input.toString());
                    } catch (Exception e) {
                        throw new CoercingParseValueException(
                                "Unable to parse value to 'java.util.Locale' because of: " + e.getMessage());
                    }
                } else if (input instanceof Locale) {
                    return (Locale) input;
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'java.lang.String' object but was " + typeName(input));
                }
            }


            @Override
            public Locale parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (input instanceof StringValue) {
                    return Locale.forLanguageTag(((StringValue) input).getValue());
                } else {
                    throw new CoercingParseLiteralException(
                            "Expected a 'java.lang.String' object but was " + typeName(input));
                }
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String s = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(s).build();
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("Locale")
                .description("A IETF BCP 47 language tag")
                .coercing(coercing)
                .build();
    }
}
