package graphql.scalars.object;

import graphql.Assert;
import graphql.GraphQLContext;
import graphql.Internal;
import graphql.execution.CoercedVariables;
import graphql.language.ArrayValue;
import graphql.language.BooleanValue;
import graphql.language.EnumValue;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.NullValue;
import graphql.language.ObjectField;
import graphql.language.ObjectValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.language.VariableReference;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import graphql.util.FpKit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static graphql.language.ObjectField.newObjectField;
import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#Object}
 */
@Internal
public final class ObjectScalar {

    private ObjectScalar() {
    }

    static final Coercing<Object, Object> OBJECT_COERCING = new Coercing<>() {
        @Override
        public Object serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
            return input;
        }

        @Override
        public Object parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
            return input;
        }

        @Override
        public Object parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
            if (!(input instanceof Value)) {
                throw new CoercingParseLiteralException(
                        "Expected AST type 'Value' but was '" + typeName(input) + "'."
                );
            }
            if (input instanceof FloatValue) {
                return ((FloatValue) input).getValue();
            }
            if (input instanceof StringValue) {
                return ((StringValue) input).getValue();
            }
            if (input instanceof IntValue) {
                return ((IntValue) input).getValue();
            }
            if (input instanceof BooleanValue) {
                return ((BooleanValue) input).isValue();
            }
            if (input instanceof EnumValue) {
                return ((EnumValue) input).getName();
            }
            if (input instanceof VariableReference) {
                String varName = ((VariableReference) input).getName();
                return variables.get(varName);
            }
            if (input instanceof ArrayValue) {
                //noinspection rawtypes
                List<Value> values = ((ArrayValue) input).getValues();
                return values.stream()
                        .map(v -> parseLiteral(v, variables, graphQLContext, locale))
                        .collect(Collectors.toList());
            }
            if (input instanceof ObjectValue) {
                List<ObjectField> values = ((ObjectValue) input).getObjectFields();
                Map<String, Object> parsedValues = new LinkedHashMap<>();
                values.forEach(fld -> {
                    Object parsedValue;
                    if (fld.getValue() instanceof NullValue) { // Nested NullValue inside ObjectValue
                        parsedValue = null;
                    } else {
                        parsedValue = parseLiteral(fld.getValue(), variables, graphQLContext, locale);
                    }
                    parsedValues.put(fld.getName(), parsedValue);
                });
                return parsedValues;
            }
            return Assert.assertShouldNeverHappen("We have covered all Value types");
        }

        @Override
        public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
            if (input == null) {
                return NullValue.newNullValue().build();
            }
            if (input instanceof String) {
                return new StringValue((String) input);
            }
            if (input instanceof Float) {
                return new FloatValue(BigDecimal.valueOf((Float) input));
            }
            if (input instanceof Double) {
                return new FloatValue(BigDecimal.valueOf((Double) input));
            }
            if (input instanceof BigDecimal) {
                return new FloatValue((BigDecimal) input);
            }
            if (input instanceof BigInteger) {
                return new IntValue((BigInteger) input);
            }
            if (input instanceof Number) {
                long l = ((Number) input).longValue();
                return new IntValue(BigInteger.valueOf(l));
            }
            if (input instanceof Boolean) {
                return new BooleanValue((Boolean) input);
            }
            if (FpKit.isIterable(input)) {
                return handleIterable(FpKit.toIterable(input), graphQLContext, locale);
            }
            if (input instanceof Map) {
                return handleMap((Map<?, ?>) input, graphQLContext, locale);
            }
            throw new UnsupportedOperationException("The ObjectScalar cant handle values of type : " + input.getClass());
        }

        private Value<?> handleMap(Map<?, ?> map, GraphQLContext graphQLContext, Locale locale) {
            ObjectValue.Builder builder = ObjectValue.newObjectValue();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String name = String.valueOf(entry.getKey());
                Value<?> value = valueToLiteral(entry.getValue(), graphQLContext, locale);

                builder.objectField(
                        newObjectField().name(name).value(value).build()
                );
            }
            return builder.build();
        }

        @SuppressWarnings("rawtypes")
        private Value<?> handleIterable(Iterable<?> input, GraphQLContext graphQLContext, Locale locale) {
            List<Value> values = new ArrayList<>();
            for (Object val : input) {
                values.add(valueToLiteral(val, graphQLContext, locale));
            }
            return ArrayValue.newArrayValue().values(values).build();
        }
    };

    public static GraphQLScalarType INSTANCE = GraphQLScalarType.newScalar()
            .name("Object")
            .description("An object scalar")
            .coercing(OBJECT_COERCING)
            .build();
}
