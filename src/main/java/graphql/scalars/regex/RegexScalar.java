package graphql.scalars.regex;

import graphql.Assert;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static graphql.scalars.util.Kit.typeName;

/**
 * This is really a scalar factory for creating new scalar String types that are based on a value matching
 * a regular expression.
 */
public class RegexScalar extends GraphQLScalarType {

    private RegexScalar(String name, String description, Coercing coercing) {
        super(name, description, coercing);
    }

    /**
     * Creates a new scalar that uses the specified {@link java.util.regex.Pattern} to control
     * the values that are allowed.
     *
     * @param name    the name of the scalar
     * @param pattern the allowable values pattern
     *
     * @return a new regex scalar
     */
    public static RegexScalar regexScalar(String name, Pattern pattern) {
        return regexScalarImpl(name, null, pattern);
    }

    /**
     * Creates a new scalar that uses the specified {@link java.util.regex.Pattern} to control
     * the values that are allowed.
     *
     * @param name        the name of the scalar
     * @param description the description of the scalar
     * @param pattern     the allowable values pattern
     *
     * @return a new regex scalar
     */
    public static RegexScalar regexScalar(String name, String description, Pattern pattern) {
        return regexScalarImpl(name, description, pattern);
    }

    /**
     * Creates a new scalar that uses the specified {@link java.util.regex.Pattern}s to control
     * the values that are allowed.
     *
     * @param name     the name of the scalar
     * @param patterns the allowable values patterns
     *
     * @return a new regex scalar
     */
    public static RegexScalar regexScalar(String name, Pattern... patterns) {
        return regexScalarImpl(name, null, patterns);
    }

    /**
     * Creates a new scalar that uses the specified {@link java.util.regex.Pattern}s to control
     * the values that are allowed.
     *
     * @param name        the name of the scalar
     * @param description the description of the scalar
     * @param patterns    the allowable values patterns
     *
     * @return a new regex scalar
     */
    public static RegexScalar regexScalar(String name, String description, Pattern... patterns) {
        return regexScalarImpl(name, description, patterns);
    }

    private static RegexScalar regexScalarImpl(String name, String description, Pattern... patterns) {
        Assert.assertNotNull(patterns);
        return new RegexScalar(name, description, new Coercing<String, String>() {
            @Override
            public String serialize(Object input) throws CoercingSerializeException {
                String value = String.valueOf(input);
                return matches(value, CoercingSerializeException::new);
            }

            @Override
            public String parseValue(Object input) throws CoercingParseValueException {
                String value = String.valueOf(input);
                return matches(value, CoercingParseValueException::new);
            }

            @Override
            public String parseLiteral(Object input) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                String value = ((StringValue) input).getValue();
                return matches(value, CoercingParseLiteralException::new);
            }

            private String matches(String value, Function<String, RuntimeException> exceptionMaker) {
                for (Pattern pattern : patterns) {
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.matches()) {
                        return value;
                    }
                }
                throw exceptionMaker.apply("Unable to accept a value into the '" + name + "' scalar.  It does not match the regular expressions.");
            }
        });
    }
}
