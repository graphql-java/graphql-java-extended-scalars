package graphql.scalars.datetime;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

public class LocalTimeCoercing implements Coercing<LocalTime, String> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public String serialize(final Object input) throws CoercingSerializeException {
        TemporalAccessor temporalAccessor;
        if (input instanceof TemporalAccessor) {
            temporalAccessor = (TemporalAccessor) input;
        } else if (input instanceof String) {
            temporalAccessor = parseTime(input.toString(), CoercingSerializeException::new);
        } else {
            throw new CoercingSerializeException(
                    "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
            );
        }
        try {
            return DATE_FORMATTER.format(temporalAccessor);
        } catch (DateTimeException e) {
            throw new CoercingSerializeException(
                    "Unable to turn TemporalAccessor into full time because of : '" + e.getMessage() + "'."
            );
        }
    }

    @Override
    public LocalTime parseValue(final Object input) throws CoercingParseValueException {
        TemporalAccessor temporalAccessor;
        if (input instanceof TemporalAccessor) {
            temporalAccessor = (TemporalAccessor) input;
        } else if (input instanceof String) {
            temporalAccessor = parseTime(input.toString(), CoercingParseValueException::new);
        } else {
            throw new CoercingParseValueException(
                    "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
            );
        }
        try {
            return LocalTime.from(temporalAccessor);
        } catch (DateTimeException e) {
            throw new CoercingParseValueException(
                    "Unable to turn TemporalAccessor into full time because of : '" + e.getMessage() + "'."
            );
        }
    }

    @Override
    public LocalTime parseLiteral(final Object input) throws CoercingParseLiteralException {
        if (!(input instanceof StringValue)) {
            throw new CoercingParseLiteralException(
                    "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
            );
        }
        return parseTime(((StringValue) input).getValue(), CoercingParseLiteralException::new);
    }

    private static LocalTime parseTime(String s, Function<String, RuntimeException> exceptionMaker) {
        try {
            TemporalAccessor temporalAccessor = DATE_FORMATTER.parse(s);
            return LocalTime.from(temporalAccessor);
        } catch (DateTimeParseException e) {
            throw exceptionMaker.apply("Invalid local time value : '" + s + "'. because of : '" + e.getMessage() + "'");
        }
    }
}
