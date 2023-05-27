package graphql.scalars.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.*;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#LocalDateTime}
 */
@Internal
public final class LocalDateTimeScalar {

    public static final GraphQLScalarType INSTANCE;

    private LocalDateTimeScalar() {}
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        Coercing<LocalDateTime, String> coercing = new Coercing<LocalDateTime, String>() {
            @Override
            public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
                try {
                    LocalDateTime localDateTime = (LocalDateTime) dataFetcherResult;
                    return localDateTime.format(DATE_TIME_FORMATTER);
                } catch (ClassCastException exception) {
                    throw new CoercingSerializeException("Input is not a LocalDateTime", exception);
                }
            }

            @Override
            public LocalDateTime parseValue(Object input) throws CoercingParseValueException {
                // Will be String if the value is specified via external variables object, and a StringValue
                // if provided direct in the query.
                if (input instanceof StringValue) {
                    return parseString(((StringValue) input).getValue());
                }
                if (input instanceof String) {
                    return parseString((String) input);
                }
                if (input instanceof LocalDateTime) {
                    return (LocalDateTime) input;
                }
                throw new CoercingParseValueException(format("Unable to parse %s as LocalDateTime", input));
            }

            private LocalDateTime parseString(String input) {
                try {
                    return LocalDateTime.parse(input, DATE_TIME_FORMATTER);
                } catch (DateTimeParseException parseException) {
                    throw new CoercingParseValueException(
                        format("Unable to parse %s as LocalDateTime", input), parseException);
                }
            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                String s = serialize(input);
                return StringValue.newStringValue(s).build();
            }

            @Override
            public LocalDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                try {
                    return parseValue(input);
                } catch (CoercingParseValueException exception) {
                    throw new CoercingParseLiteralException(exception);
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("LocalDateTime")
                .description("A date-time without a time-zone in the ISO-8601 calendar system, formatted as 2007-12-03 10:15:30")
                .specifiedByUrl("https://scalars.graphql.org/andimarek/local-date-time") // TODO: Change to .specifiedByURL when builder added to graphql-java
                .coercing(coercing)
                .build();
    }
}
