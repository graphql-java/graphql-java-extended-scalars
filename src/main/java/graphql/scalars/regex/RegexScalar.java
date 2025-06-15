package graphql.scalars.regex;

import graphql.Assert;
import graphql.GraphQLContext;
import graphql.PublicApi;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static graphql.scalars.util.Kit.typeName;

/**
 * This is really a scalar factory for creating new scalar String types that are based on a value matching
 * a regular expression.
 */
@PublicApi
public final class RegexScalar {

    private RegexScalar() {
    }

    /**
     * A builder for {@link graphql.scalars.regex.RegexScalar}
     */
    public static class Builder {
        private String name;
        private String description;
        private final List<Pattern> patterns = new ArrayList<>();

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
        public GraphQLScalarType build() {
            Assert.assertNotNull(name);
            return regexScalarImpl(name, description, patterns);
        }
    }

    private static GraphQLScalarType regexScalarImpl(String name, String description, List<Pattern> patterns) {
        Assert.assertNotNull(patterns);

        Coercing<String, String> coercing = new Coercing<>() {
            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                String value = String.valueOf(input);
                return matches(value, CoercingSerializeException::new);
            }

            @Override
            public String parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                String value = String.valueOf(input);
                return matches(value, CoercingParseValueException::new);
            }

            @Override
            public String parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                String value = ((StringValue) input).getValue();
                return matches(value, CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String s = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(s).build();
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
        };

        return GraphQLScalarType.newScalar()
                .name(name)
                .description(description)
                .coercing(coercing)
                .build();
    }
}
