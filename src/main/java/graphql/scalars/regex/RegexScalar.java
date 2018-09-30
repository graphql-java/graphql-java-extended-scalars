package graphql.scalars.regex;

import graphql.Assert;
import graphql.PublicApi;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static graphql.scalars.util.Kit.typeName;

/**
 * This is really a scalar factory for creating new scalar String types that are based on a value matching
 * a regular expression.
 */
@PublicApi
public class RegexScalar extends GraphQLScalarType {

    /**
     * A builder for {@link graphql.scalars.regex.RegexScalar}
     */
    public static class Builder {
        private String name;
        private String description;
        private List<Pattern> patterns = new ArrayList<>();

        /**
         * Sets the name of the regex scalar
         *
         * @param name the name of the regex scalar
         *
         * @return this builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the description of the regex scalar
         *
         * @param description the description of the regex scalar
         *
         * @return this builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Adds a {@link java.util.regex.Pattern} that controls the acceptable value for this scalar
         *
         * @param pattern the regex pattern
         *
         * @return this builder
         */
        public Builder addPattern(Pattern pattern) {
            this.patterns.add(pattern);
            return this;
        }

        /**
         * Adds a {@link java.util.regex.Pattern} that controls the acceptable value for this scalar
         *
         * @param patterns one of more regex patterns
         *
         * @return this builder
         */
        public Builder addPatterns(Pattern... patterns) {
            Collections.addAll(this.patterns, patterns);
            return this;
        }

        /**
         * @return the built {@link graphql.scalars.regex.RegexScalar}
         */
        public RegexScalar build() {
            Assert.assertNotNull(name);
            return regexScalarImpl(name, description, patterns);
        }
    }

    private RegexScalar(String name, String description, Coercing coercing) {
        super(name, description, coercing);
    }

    private static RegexScalar regexScalarImpl(String name, String description, List<Pattern> patterns) {
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
