package graphql.scalars.uri;

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

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

@Internal
public final class UriScalar {

    private UriScalar() {
    }

    public static final GraphQLScalarType INSTANCE;

    static {
        Coercing<URI, URI> coercing = new Coercing<>() {
            @Override
            public URI serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                Optional<URI> uri;
                if (input instanceof String) {
                    uri = Optional.of(parseURI(input.toString(), CoercingSerializeException::new));
                } else {
                    uri = toURI(input);
                }
                if (uri.isPresent()) {
                    return uri.get();
                }
                throw new CoercingSerializeException(
                        "Expected a 'URI' like object but was '" + typeName(input) + "'."
                );
            }

            @Override
            public URI parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                String uriStr;
                if (input instanceof String) {
                    uriStr = String.valueOf(input);
                } else {
                    Optional<URI> uri = toURI(input);
                    if (uri.isEmpty()) {
                        throw new CoercingParseValueException(
                                "Expected a 'URI' like object but was '" + typeName(input) + "'."
                        );
                    }
                    return uri.get();
                }
                return parseURI(uriStr, CoercingParseValueException::new);
            }

            @Override
            public URI parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseURI(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                URI uri = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(uri.toString()).build();
            }


            private URI parseURI(String input, Function<String, RuntimeException> exceptionMaker) {
                try {
                    return new URI(input);
                } catch (URISyntaxException e) {
                    throw exceptionMaker.apply("Invalid URI value : '" + input + "'.");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("Uri")
                .description("A Uri scalar")
                .coercing(coercing)
                .build();
    }

    private static Optional<URI> toURI(Object input) {
        if (input instanceof URI) {
            return Optional.of((URI) input);
        } else if (input instanceof URL) {
            try {
                return Optional.of(((URL) input).toURI());
            } catch (URISyntaxException ignored) {
            }
        } else if (input instanceof File) {
            return Optional.of(((File) input).toURI());
        }
        return Optional.empty();
    }

}
