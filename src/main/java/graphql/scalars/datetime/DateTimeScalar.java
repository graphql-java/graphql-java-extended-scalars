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
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;
import static java.time.temporal.ChronoField.OFFSET_SECONDS;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#DateTime}
 */
@Internal
public final class DateTimeScalar {

    public static final GraphQLScalarType INSTANCE;

    private DateTimeScalar() {}
    private static final DateTimeFormatter customOutputFormatter = getCustomDateTimeFormatter();

    static {
        Coercing<OffsetDateTime, String> coercing = new Coercing<OffsetDateTime, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                OffsetDateTime offsetDateTime;
                if (input instanceof OffsetDateTime) {
                    offsetDateTime = (OffsetDateTime) input;
                } else if (input instanceof ZonedDateTime) {
                    offsetDateTime = ((ZonedDateTime) input).toOffsetDateTime();
                } else if (input instanceof String) {
                    offsetDateTime = parseOffsetDateTime(input.toString(), CoercingSerializeException::new);
                } else {
                    throw new CoercingSerializeException(
                            "Expected something we can convert to 'java.time.OffsetDateTime' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return customOutputFormatter.format(offsetDateTime);
                } catch (DateTimeException e) {
                    throw new CoercingSerializeException(
                            "Unable to turn TemporalAccessor into OffsetDateTime because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public OffsetDateTime parseValue(Object input) throws CoercingParseValueException {
                OffsetDateTime offsetDateTime;
                if (input instanceof OffsetDateTime) {
                    offsetDateTime = (OffsetDateTime) input;
                } else if (input instanceof ZonedDateTime) {
                    offsetDateTime = ((ZonedDateTime) input).toOffsetDateTime();
                } else if (input instanceof String) {
                    offsetDateTime = parseOffsetDateTime(input.toString(), CoercingParseValueException::new);
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'String' but was '" + typeName(input) + "'."
                    );
                }
                return offsetDateTime;
            }

            @Override
            public OffsetDateTime parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseOffsetDateTime(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                String s = serialize(input);
                return StringValue.newStringValue(s).build();
            }

            private OffsetDateTime parseOffsetDateTime(String s, Function<String, RuntimeException> exceptionMaker) {
                try {
                    OffsetDateTime parse = OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    if (parse.get(OFFSET_SECONDS) == 0 && s.endsWith("-00:00")) {
                        throw exceptionMaker.apply("Invalid value : '" + s + "'. Negative zero offset is not allowed");
                    }
                    return parse;
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid RFC3339 value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("A slightly refined version of RFC-3339 compliant DateTime Scalar")
                .specifiedByUrl("https://scalars.graphql.org/andimarek/date-time") // TODO: Change to .specifiedByURL after release of graphql-java v20
                .coercing(coercing)
                .build();
    }

    private static DateTimeFormatter getCustomDateTimeFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .appendFraction(NANO_OF_SECOND, 3, 3, true)
                .appendOffset("+HH:MM", "Z")
                .toFormatter();
    }

}
