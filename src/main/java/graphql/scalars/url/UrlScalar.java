package graphql.scalars.url;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import okhttp3.HttpUrl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

@Internal
public class UrlScalar extends GraphQLScalarType {

    public UrlScalar() {
        super("Url", "A Url scalar", new Coercing<URL, URL>() {
            @Override
            public URL serialize(Object input) throws CoercingSerializeException {
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
            public URL parseValue(Object input) throws CoercingParseValueException {
                String urlStr;
                if (input instanceof String) {
                    urlStr = String.valueOf(input);
                } else {
                    Optional<URL> url = toURL(input);
                    if (!url.isPresent()) {
                        throw new CoercingParseValueException(
                                "Expected a 'URL' like object but was '" + typeName(input) + "'."
                        );
                    }
                    return url.get();
                }
                return parseURL(urlStr, CoercingParseValueException::new);
            }

            @Override
            public URL parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseURL(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            private URL parseURL(String input, Function<String, RuntimeException> exceptionMaker) {
                HttpUrl httpUrl = HttpUrl.parse(input);
                if (httpUrl == null) {
                    throw exceptionMaker.apply("Invalid URL value : '" + input + "'.");
                }
                return httpUrl.url();
            }
        });
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
