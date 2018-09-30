package graphql.scalars.alias;

import graphql.Assert;
import graphql.Internal;
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
public class AliasedScalar extends GraphQLScalarType {

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
        public AliasedScalar build() {
            Assert.assertNotNull(name);
            return aliasedScalarImpl(name, description, aliasedScalar);
        }
    }


    private AliasedScalar(String name, String description, Coercing coercing) {
        super(name, description, coercing);
    }

    private static AliasedScalar aliasedScalarImpl(String name, String description, GraphQLScalarType aliasedScalar) {
        Assert.assertNotNull(aliasedScalar);
        return new AliasedScalar(name, description, new Coercing<Object, Object>() {
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
                return aliasedScalar.getCoercing().parseLiteral(input, variables);
            }
        });
    }
}
