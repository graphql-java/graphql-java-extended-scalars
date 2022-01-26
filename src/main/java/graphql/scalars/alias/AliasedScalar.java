package graphql.scalars.alias;

import graphql.Assert;
import graphql.Internal;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.Map;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#newAliasedScalar(String)}
 */
@Internal
public final class AliasedScalar {

    private AliasedScalar() {}

    /**
     * A builder for {@link graphql.scalars.alias.AliasedScalar}
     */
    public static class Builder {
        private String name;
        private String description;
        private GraphQLScalarType aliasedScalar;

        /**
         * Sets the name of the aliased scalar
         *
         * @param name the name of the aliased scalar
         *
         * @return this builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the description of the aliased scalar
         *
         * @param description the description of the aliased scalar
         *
         * @return this builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Sets the scalar that is to be aliased
         *
         * @param aliasedScalar the scalar that is to be aliased
         *
         * @return this builder
         */
        public Builder aliasedScalar(GraphQLScalarType aliasedScalar) {
            this.aliasedScalar = aliasedScalar;
            return this;
        }

        /**
         * @return the built {@link AliasedScalar}
         */
        public GraphQLScalarType build() {
            Assert.assertNotNull(name);
            return aliasedScalarImpl(name, description, aliasedScalar);
        }
    }


    private static GraphQLScalarType aliasedScalarImpl(String name, String description, GraphQLScalarType aliasedScalar) {
        Assert.assertNotNull(aliasedScalar);
        Coercing<Object, Object> coercing = new Coercing<Object, Object>() {
            @Override
            public Object serialize(Object input) throws CoercingSerializeException {
                return aliasedScalar.getCoercing().serialize(input);
            }

            @Override
            public Object parseValue(Object input) throws CoercingParseValueException {
                return aliasedScalar.getCoercing().parseValue(input);
            }

            @Override
            public Object parseLiteral(Object input) throws CoercingParseLiteralException {
                return aliasedScalar.getCoercing().parseLiteral(input);
            }

            @Override
            public Object parseLiteral(Object input, Map<String, Object> variables) throws CoercingParseLiteralException {
                Coercing<?, ?> c = aliasedScalar.getCoercing();
                return c.parseLiteral(input, variables);
            }

            @Override
            public Value<?> valueToLiteral(Object input) {
                return aliasedScalar.getCoercing().valueToLiteral(input);
            }
        };
        return GraphQLScalarType.newScalar()
                .name(name)
                .description(description)
                .coercing(coercing)
                .build();
    }
}
