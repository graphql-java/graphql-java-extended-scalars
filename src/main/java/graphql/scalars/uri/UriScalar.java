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
import java.net.MalformedURLException;
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
        Coercing<URL, URL> coercing = new Coercing<>() {
            @Override
            public URL serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                Optional<URL> url;
                if (input instanceof String) {
                    url = Optional.of(parseURL(input.toString(), CoercingSerializeException::new));
                } else {
                    url = toURL(input);
                }
                if (url.isPresent()) {
                    return url.get();
                }
                throw new CoercingSerializeException(
                        "Expected a 'URL' like object but was '" + typeName(input) + "'."
                );
            }

            @Override
            public URL parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                String urlStr;
                if (input instanceof String) {
                    urlStr = String.valueOf(input);
                } else {
                    Optional<URL> url = toURL(input);
                    if (url.isEmpty()) {
                        throw new CoercingParseValueException(
                                "Expected a 'URL' like object but was '" + typeName(input) + "'."
                        );
                    }
                    return url.get();
                }
                return parseURL(urlStr, CoercingParseValueException::new);
            }

            @Override
            public URL parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseURL(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                URL url = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(url.toExternalForm()).build();
            }


            private URL parseURL(String input, Function<String, RuntimeException> exceptionMaker) {
                try {
                    return new URI(input).toURL();
                } catch (URISyntaxException | IllegalArgumentException | MalformedURLException e) {
                    throw exceptionMaker.apply("Invalid URL value : '" + input + "'.");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("Url")
                .description("A Url scalar")
                .coercing(coercing)
                .build();
    }

    private static Optional<URL> toURL(Object input) {
        if (input instanceof URL) {
            return Optional.of((URL) input);
        } else if (input instanceof URI) {
            try {
                return Optional.of(((URI) input).toURL());
            } catch (MalformedURLException ignored) {
            }
        } else if (input instanceof File) {
            try {
                return Optional.of(((File) input).toURI().toURL());
            } catch (MalformedURLException ignored) {
            }
        }
        return Optional.empty();
    }

}
