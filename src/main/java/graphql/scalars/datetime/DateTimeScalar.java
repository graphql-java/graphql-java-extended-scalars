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
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#DateTime}
 */
@Internal
public class DateTimeScalar {

    public static GraphQLScalarType INSTANCE;

    static {
        Coercing<OffsetDateTime, String> coercing = new Coercing<OffsetDateTime, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                OffsetDateTime offsetDateTime;
                if (input instanceof OffsetDateTime) {
                    offsetDateTime = (OffsetDateTime) input;
                } else if (input instanceof ZonedDateTime) {
                    offsetDateTime = ((ZonedDateTime) input).toOffsetDateTime();
                } else if (input instanceof Instant) {
                    offsetDateTime = ((Instant) input).atOffset(ZoneOffset.UTC);
                } else if (input instanceof Date) {
                    offsetDateTime = ((Date) input).toInstant().atOffset(ZoneOffset.UTC);
                } else if (input instanceof String) {
                    offsetDateTime = parseOffsetDateTime(input.toString(), CoercingSerializeException::new);
                } else {
                    throw new CoercingSerializeException(
                            "Expected something we can convert to 'java.time.OffsetDateTime' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime);
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
                } else if (input instanceof Instant) {
                    offsetDateTime = ((Instant) input).atOffset(ZoneOffset.UTC);
                } else if (input instanceof Date) {
                    offsetDateTime = ((Date) input).toInstant().atOffset(ZoneOffset.UTC);
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
                    return OffsetDateTime.parse(s, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid RFC3339 value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("DateTime")
                .description("An RFC-3339 compliant DateTime Scalar")
                .coercing(coercing)
                .build();
    }

}
