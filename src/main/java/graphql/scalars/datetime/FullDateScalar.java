package graphql.scalars.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

@Internal
public class FullDateScalar extends GraphQLScalarType {

    private final static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ;

    static Coercing<LocalDate, String> RFC3339_DATE_COERCING = new Coercing<LocalDate, String>() {
        @Override
        public String serialize(Object input) throws CoercingSerializeException {
            if (!(input instanceof TemporalAccessor)) {
                throw new CoercingParseValueException(
                        "Expected a 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
                );
            }
            TemporalAccessor temporalAccessor = (TemporalAccessor) input;
            try {
                return dateFormatter.format(temporalAccessor);
            } catch (DateTimeException e) {
                throw new CoercingSerializeException(
                        "Unable to turn TemporalAccessor into full date because of : '" + e.getMessage() + "'."
                );
            }
        }

        @Override
        public LocalDate parseValue(Object input) throws CoercingParseValueException {
            if (!(input instanceof String)) {
                throw new CoercingParseValueException(
                        "Expected a 'String' but was '" + typeName(input) + "'."
                );
            }
            return parseStr(input.toString(), CoercingParseValueException::new);
        }

        @Override
        public LocalDate parseLiteral(Object input) throws CoercingParseLiteralException {
            if (!(input instanceof StringValue)) {
                throw new CoercingParseLiteralException(
                        "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                );
            }
            return parseStr(((StringValue) input).getValue(), CoercingParseLiteralException::new);
        }

        private LocalDate parseStr(String s, Function<String, RuntimeException> exceptionMaker) {
            try {
                TemporalAccessor temporalAccessor = dateFormatter.parse(s);
                return LocalDate.from(temporalAccessor);
            } catch (DateTimeParseException e) {
                throw exceptionMaker.apply("Invalid RFC3339 full date value : '" + s + "'. because of : '" + e.getMessage() + "'");
            }
        }
    };

    public FullDateScalar() {
        super("Date", "An RFC-3339 compliant Full Date Scalar", RFC3339_DATE_COERCING);
    }
}
