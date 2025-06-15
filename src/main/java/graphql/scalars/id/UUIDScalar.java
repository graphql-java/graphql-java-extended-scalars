package graphql.scalars.id;

import graphql.GraphQLContext;
import graphql.Internal;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.scalars.util.Kit;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.Locale;
import java.util.UUID;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#UUID}
 */
@Internal
public class UUIDScalar {

    public static GraphQLScalarType INSTANCE;

    static {
        Coercing<UUID, String> coercing = new Coercing<>() {
            private UUID convertImpl(Object input) {
                if (input instanceof String) {
                    try {
                        return (UUID.fromString((String) input));
                    } catch (IllegalArgumentException ex) {
                        return null;
                    }
                } else if (input instanceof UUID) {
                    return (UUID) input;
                }
                return null;
            }

            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                UUID result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'UUID' but was '" + Kit.typeName(input) + "'."
                    );
                }
                return result.toString();
            }

            @Override
            public UUID parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                UUID result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'UUID' but was '" + Kit.typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public UUID parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected a 'java.util.UUID' AST type object but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return UUID.fromString(((StringValue) input).getValue());
                } catch (IllegalArgumentException ex) {
                    throw new CoercingParseLiteralException(
                            "Expected something that we can convert to a UUID but was invalid"
                    );
                }
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String s = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(s).build();
            }
        };


        INSTANCE = GraphQLScalarType.newScalar()
                .name("UUID")
                .description("A universally unique identifier compliant UUID Scalar")
                .coercing(coercing)
                .build();

    }

}
