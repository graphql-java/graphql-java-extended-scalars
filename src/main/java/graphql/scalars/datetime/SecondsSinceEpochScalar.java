package graphql.scalars.datetime;

import graphql.GraphQLContext;
import graphql.Internal;
import graphql.execution.CoercedVariables;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#SecondsSinceEpoch}
 */
@Internal
public final class SecondsSinceEpochScalar {

    public static final GraphQLScalarType INSTANCE;

    private SecondsSinceEpochScalar() {
    }

    private static Temporal convertToTemporal(String value) {
        try {
            if (value.matches("\\d+")) {
                long epochSeconds = Long.parseLong(value);
                return convertEpochSecondsToTemporal(epochSeconds);
            }
            throw new CoercingParseValueException(
                    "Invalid seconds since epoch value : '" + value + "'. Expected a string containing only digits."
            );
        } catch (Exception e) {
            throw new CoercingParseValueException(
                    "Invalid seconds since epoch value : '" + value + "'. " + e.getMessage()
            );
        }
    }

    private static Temporal convertEpochSecondsToTemporal(long epochSeconds) {
        return Instant.ofEpochSecond(epochSeconds).atZone(ZoneOffset.UTC);
    }

    static {
        Coercing<TemporalAccessor, Long> coercing = new Coercing<>() {
            @Override
            public Long serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                try {
                    if (input instanceof Number) {
                        Number number = (Number) input;
                        return number.longValue();
                    }
                    if (input instanceof String) {
                        try {
                            return Long.parseLong((String) input);
                        } catch (NumberFormatException e) {
                            throw new CoercingSerializeException(
                                    "Invalid seconds since epoch value : '" + input + "'. Expected a string containing only digits.",
                                    e
                            );
                        }
                    }
                    if (input instanceof TemporalAccessor) {
                        TemporalAccessor temporalAccessor = (TemporalAccessor) input;
                        if (temporalAccessor instanceof Instant) {
                            Instant instant = (Instant) temporalAccessor;
                            return instant.getEpochSecond();
                        } else if (temporalAccessor instanceof LocalDateTime) {
                            LocalDateTime localDateTime = (LocalDateTime) temporalAccessor;
                            return localDateTime.toEpochSecond(ZoneOffset.UTC);
                        } else if (temporalAccessor instanceof ZonedDateTime) {
                            ZonedDateTime zonedDateTime = (ZonedDateTime) temporalAccessor;
                            return zonedDateTime.toEpochSecond();
                        } else if (temporalAccessor instanceof OffsetDateTime) {
                            OffsetDateTime offsetDateTime = (OffsetDateTime) temporalAccessor;
                            return offsetDateTime.toEpochSecond();
                        } else {
                            try {
                                Instant instant = Instant.from(temporalAccessor);
                                return instant.getEpochSecond();
                            } catch (Exception e) {
                                throw new CoercingSerializeException(
                                        "Unable to convert TemporalAccessor to seconds since epoch because of : '" + e.getMessage() + "'."
                                );
                            }
                        }
                    }
                    throw new CoercingSerializeException(
                            "Expected a 'Number', 'String' or 'TemporalAccessor' but was '" + typeName(input) + "'."
                    );
                } catch (CoercingSerializeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new CoercingSerializeException(
                            "Unable to convert to seconds since epoch because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public TemporalAccessor parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                try {
                    if (input instanceof Number) {
                        Number number = (Number) input;
                        return convertEpochSecondsToTemporal(number.longValue());
                    }
                    if (input instanceof String) {
                        String string = (String) input;
                        return convertToTemporal(string);
                    }
                    throw new CoercingParseValueException(
                            "Expected a 'Number' or 'String' but was '" + typeName(input) + "'."
                    );
                } catch (CoercingParseValueException e) {
                    throw e;
                } catch (Exception e) {
                    throw new CoercingParseValueException(
                            "Unable to parse value to seconds since epoch because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public TemporalAccessor parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                try {
                    if (input instanceof StringValue) {
                        StringValue stringValue = (StringValue) input;
                        return convertToTemporal(stringValue.getValue());
                    }
                    if (input instanceof IntValue) {
                        IntValue intValue = (IntValue) input;
                        long epochSeconds = intValue.getValue().longValue();
                        return convertEpochSecondsToTemporal(epochSeconds);
                    }
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' or 'IntValue' but was '" + typeName(input) + "'."
                    );
                } catch (CoercingParseLiteralException e) {
                    throw e;
                } catch (Exception e) {
                    throw new CoercingParseLiteralException(
                            "Unable to parse literal to seconds since epoch because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                Long value = serialize(input, graphQLContext, locale);
                return IntValue.newIntValue(java.math.BigInteger.valueOf(value)).build();
            }

        };

        INSTANCE = GraphQLScalarType.newScalar()
                                    .name("SecondsSinceEpoch")
                                    .description("Scalar that represents a point in time as seconds since the Unix epoch (Unix timestamp). " +
                                            "Accepts integers or strings containing integers as input values. " +
                                            "Returns a Long representing the number of seconds since epoch (January 1, 1970, 00:00:00 UTC).")
                                    .coercing(coercing)
                                    .build();
    }
}
