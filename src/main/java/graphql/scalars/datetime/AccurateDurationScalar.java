package graphql.scalars.datetime;

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

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#AccurateDuration}
 */
@Internal
public class AccurateDurationScalar {

    public static final GraphQLScalarType INSTANCE;

    private AccurateDurationScalar() {
    }

    static {
        Coercing<Duration, String> coercing = new Coercing<>() {
            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                Duration duration;
                if (input instanceof Duration) {
                    duration = (Duration) input;
                } else if (input instanceof String) {
                    duration = parseDuration(input.toString(), CoercingSerializeException::new);
                } else {
                    throw new CoercingSerializeException(
                            "Expected something we can convert to 'java.time.Duration' but was '" + typeName(input) + "'."
                    );
                }
                return duration.toString();
            }

            @Override
            public Duration parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                Duration duration;
                if (input instanceof Duration) {
                    duration = (Duration) input;
                } else if (input instanceof String) {
                    duration = parseDuration(input.toString(), CoercingParseValueException::new);
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'String' but was '" + typeName(input) + "'."
                    );
                }
                return duration;
            }

            @Override
            public Duration parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseDuration(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String s = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(s).build();
            }

            private Duration parseDuration(String s, Function<String, RuntimeException> exceptionMaker) {
                try {
                    return Duration.parse(s);
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid ISO 8601 value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("AccurateDuration")
                .description("A ISO 8601 duration scalar with only day, hour, minute, second components.")
                .specifiedByUrl("https://scalars.graphql.org/AlexandreCarlton/accurate-duration") // TODO: Change to .specifiedByURL when builder added to graphql-java
                .coercing(coercing)
                .build();
    }

}
