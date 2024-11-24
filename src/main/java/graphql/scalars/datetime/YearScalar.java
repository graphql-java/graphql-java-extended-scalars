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
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#Year}
 */
@Internal
public final class YearScalar  {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    public static final GraphQLScalarType INSTANCE;

    private YearScalar() {}

    static {
        Coercing<Year, String> coercing = new Coercing<Year, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                TemporalAccessor temporalAccessor;
                if (input instanceof TemporalAccessor) {
                    temporalAccessor = (TemporalAccessor) input;
                } else if (input instanceof String) {
                    temporalAccessor = parseYear(input.toString(), CoercingSerializeException::new);
                } else {
                    throw new CoercingSerializeException(
                            "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return DATE_FORMATTER.format(temporalAccessor);
                } catch (DateTimeException e) {
                    throw new CoercingSerializeException(
                            "Unable to turn TemporalAccessor into full year because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public Year parseValue(Object input) throws CoercingParseValueException {
                TemporalAccessor temporalAccessor;
                if (input instanceof TemporalAccessor) {
                    temporalAccessor = (TemporalAccessor) input;
                } else if (input instanceof String) {
                    temporalAccessor = parseYear(input.toString(), CoercingParseValueException::new);
                } else {
                    throw new CoercingParseValueException(
                            "Expected a 'String' or 'java.time.temporal.TemporalAccessor' but was '" + typeName(input) + "'."
                    );
                }
                try {
                    return Year.from(temporalAccessor);
                } catch (DateTimeException e) {
                    throw new CoercingParseValueException(
                            "Unable to turn TemporalAccessor into full year because of : '" + e.getMessage() + "'."
                    );
                }
            }

            @Override
            public Year parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parseYear(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                String s = serialize(input);
                return StringValue.newStringValue(s).build();
            }

            private Year parseYear(String s, Function<String, RuntimeException> exceptionMaker) {
                try {
                    TemporalAccessor temporalAccessor = DATE_FORMATTER.parse(s);
                    return Year.from(temporalAccessor);
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid RFC3339 full year value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("Year")
                .description("An RFC-3339 compliant Full Year Scalar")
                .coercing(coercing)
                .build();
    }
}
