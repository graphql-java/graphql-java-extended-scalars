package graphql.scalars.datetime;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.function.Function;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#NominalDuration}
 */
@Internal
public class NominalDurationScalar {

    public static final GraphQLScalarType INSTANCE;

    private NominalDurationScalar() {}

    static {
        Coercing<Period, String> coercing = new Coercing<Period, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                Period period;
                if (input instanceof Period) {
                    period = (Period) input;
                } else if (input instanceof String) {
                    period = parsePeriod(input.toString(), CoercingSerializeException::new);
                } else {
                    throw new CoercingSerializeException(
                        "Expected something we can convert to 'java.time.OffsetDateTime' but was '" + typeName(input) + "'."
                    );
                }
                return period.toString();
            }

            @Override
            public Period parseValue(Object input) throws CoercingParseValueException {
                Period period;
                if (input instanceof Period) {
                    period = (Period) input;
                } else if (input instanceof String) {
                    period = parsePeriod(input.toString(), CoercingParseValueException::new);
                } else {
                    throw new CoercingParseValueException(
                        "Expected a 'String' but was '" + typeName(input) + "'."
                    );
                }
                return period;
            }

            @Override
            public Period parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                        "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                return parsePeriod(((StringValue) input).getValue(), CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                String s = serialize(input);
                return StringValue.newStringValue(s).build();
            }

            private Period parsePeriod(String s, Function<String, RuntimeException> exceptionMaker) {
                try {
                    return Period.parse(s);
                } catch (DateTimeParseException e) {
                    throw exceptionMaker.apply("Invalid ISO 8601 value : '" + s + "'. because of : '" + e.getMessage() + "'");
                }
            }
        };

        INSTANCE = GraphQLScalarType.newScalar()
            .name("NominalDuration")
            .description("A ISO 8601 duration with only year, month, week and day components.")
            .specifiedByUrl("https://scalars.graphql.org/AlexandreCarlton/nominal-duration") // TODO: Change to .specifiedByURL when builder added to graphql-java
            .coercing(coercing)
            .build();
    }
}
