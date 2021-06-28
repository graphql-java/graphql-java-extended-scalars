package graphql.scalars.java;

import graphql.Internal;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Access these via {@link graphql.scalars.ExtendedScalars}
 */
@Internal
public class JavaPrimitives {

    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger BYTE_MAX = BigInteger.valueOf(Byte.MAX_VALUE);
    private static final BigInteger BYTE_MIN = BigInteger.valueOf(Byte.MIN_VALUE);
    private static final BigInteger SHORT_MAX = BigInteger.valueOf(Short.MAX_VALUE);
    private static final BigInteger SHORT_MIN = BigInteger.valueOf(Short.MIN_VALUE);

    private static boolean isNumberIsh(Object input) {
        return input instanceof Number || input instanceof String;
    }

    private static String typeName(Object input) {
        if (input == null) {
            return "null";
        }

        return input.getClass().getSimpleName();
    }

    /**
     * This represents the "Long" type which is a representation of java.lang.Long
     */
    public static final GraphQLScalarType GraphQLLong;

    static {
        Coercing<Long, Long> longCoercing = new Coercing<Long, Long>() {

            private Long convertImpl(Object input) {
                if (input instanceof Long) {
                    return (Long) input;
                } else if (isNumberIsh(input)) {
                    BigDecimal value;
                    try {
                        value = new BigDecimal(input.toString());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    try {
                        return value.longValueExact();
                    } catch (ArithmeticException e) {
                        return null;
                    }
                } else {
                    return null;
                }

            }

            @Override
            public Long serialize(Object input) {
                Long result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'Long' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Long parseValue(Object input) {
                Long result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'Long' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Long parseLiteral(Object input) {
                if (input instanceof StringValue) {
                    try {
                        return Long.parseLong(((StringValue) input).getValue());
                    } catch (NumberFormatException e) {
                        throw new CoercingParseLiteralException(
                                "Expected value to be a Long but it was '" + input + "'"
                        );
                    }
                } else if (input instanceof IntValue) {
                    BigInteger value = ((IntValue) input).getValue();
                    if (value.compareTo(LONG_MIN) < 0 || value.compareTo(LONG_MAX) > 0) {
                        throw new CoercingParseLiteralException(
                                "Expected value to be in the Long range but it was '" + value + "'"
                        );
                    }
                    return value.longValue();
                }
                throw new CoercingParseLiteralException(
                        "Expected AST type 'IntValue' or 'StringValue' but was '" + typeName(input) + "'."
                );
            }
        };

        GraphQLLong = GraphQLScalarType.newScalar()
                .name("Long").description("Long type")
                .coercing(longCoercing).build();
    }

    /**
     * This represents the "Short" type which is a representation of java.lang.Short
     */
    public static final GraphQLScalarType GraphQLShort;

    static {
        Coercing<Short, Short> shortCoercing = new Coercing<Short, Short>() {

            private Short convertImpl(Object input) {
                if (input instanceof Short) {
                    return (Short) input;
                } else if (isNumberIsh(input)) {
                    BigDecimal value;
                    try {
                        value = new BigDecimal(input.toString());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    try {
                        return value.shortValueExact();
                    } catch (ArithmeticException e) {
                        return null;
                    }
                } else {
                    return null;
                }

            }

            @Override
            public Short serialize(Object input) {
                Short result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'Short' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Short parseValue(Object input) {
                Short result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'Short' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Short parseLiteral(Object input) {
                if (!(input instanceof IntValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'IntValue' but was '" + typeName(input) + "'."
                    );
                }
                BigInteger value = ((IntValue) input).getValue();
                if (value.compareTo(SHORT_MIN) < 0 || value.compareTo(SHORT_MAX) > 0) {
                    throw new CoercingParseLiteralException(
                            "Expected value to be in the Short range but it was '" + value + "'"
                    );
                }
                return value.shortValue();
            }
        };

        GraphQLShort = GraphQLScalarType.newScalar()
                .name("Short").description("Short as Int")
                .coercing(shortCoercing).build();
    }

    /**
     * This represents the "Byte" type which is a representation of java.lang.Byte
     */
    public static final GraphQLScalarType GraphQLByte;

    static {
        Coercing<Byte, Byte> byteCoercing = new Coercing<Byte, Byte>() {

            private Byte convertImpl(Object input) {
                if (input instanceof Byte) {
                    return (Byte) input;
                } else if (isNumberIsh(input)) {
                    BigDecimal value;
                    try {
                        value = new BigDecimal(input.toString());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    try {
                        return value.byteValueExact();
                    } catch (ArithmeticException e) {
                        return null;
                    }
                } else {
                    return null;
                }

            }

            @Override
            public Byte serialize(Object input) {
                Byte result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'Byte' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Byte parseValue(Object input) {
                Byte result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'Byte' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Byte parseLiteral(Object input) {
                if (!(input instanceof IntValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'IntValue' but was '" + typeName(input) + "'."
                    );
                }
                BigInteger value = ((IntValue) input).getValue();
                if (value.compareTo(BYTE_MIN) < 0 || value.compareTo(BYTE_MAX) > 0) {
                    throw new CoercingParseLiteralException(
                            "Expected value to be in the Byte range but it was '" + value + "'"
                    );
                }
                return value.byteValue();
            }
        };

        GraphQLByte = GraphQLScalarType.newScalar()
                .name("Byte").description("Byte as Int")
                .coercing(byteCoercing).build();
    }


    /**
     * This represents the "BigInteger" type which is a representation of java.math.BigInteger
     */
    public static final GraphQLScalarType GraphQLBigInteger;

    static {
        Coercing<BigInteger, BigInteger> bigIntCoercing = new Coercing<BigInteger, BigInteger>() {

            private BigInteger convertImpl(Object input) {
                if (isNumberIsh(input)) {
                    BigDecimal value;
                    try {
                        value = new BigDecimal(input.toString());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    try {
                        return value.toBigIntegerExact();
                    } catch (ArithmeticException e) {
                        return null;
                    }
                }
                return null;

            }

            @Override
            public BigInteger serialize(Object input) {
                BigInteger result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'BigInteger' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public BigInteger parseValue(Object input) {
                BigInteger result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'BigInteger' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public BigInteger parseLiteral(Object input) {
                if (input instanceof StringValue) {
                    try {
                        return new BigDecimal(((StringValue) input).getValue()).toBigIntegerExact();
                    } catch (NumberFormatException | ArithmeticException e) {
                        throw new CoercingParseLiteralException(
                                "Unable to turn AST input into a 'BigInteger' : '" + input + "'"
                        );
                    }
                } else if (input instanceof IntValue) {
                    return ((IntValue) input).getValue();
                } else if (input instanceof FloatValue) {
                    try {
                        return ((FloatValue) input).getValue().toBigIntegerExact();
                    } catch (ArithmeticException e) {
                        throw new CoercingParseLiteralException(
                                "Unable to turn AST input into a 'BigInteger' : '" + input + "'"
                        );
                    }
                }
                throw new CoercingParseLiteralException(
                        "Expected AST type 'IntValue', 'StringValue' or 'FloatValue' but was '" + typeName(input) + "'."
                );
            }
        };

        GraphQLBigInteger = GraphQLScalarType.newScalar()
                .name("BigInteger").description("java.math.BigInteger")
                .coercing(bigIntCoercing).build();
    }

    /**
     * This represents the "BigDecimal" type which is a representation of java.math.BigDecimal
     */
    public static final GraphQLScalarType GraphQLBigDecimal;

    static {
        Coercing<BigDecimal, BigDecimal> bigDecimalCoercing = new Coercing<BigDecimal, BigDecimal>() {

            private BigDecimal convertImpl(Object input) {
                if (isNumberIsh(input)) {
                    try {
                        return new BigDecimal(input.toString());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
                return null;

            }

            @Override
            public BigDecimal serialize(Object input) {
                BigDecimal result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'BigDecimal' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public BigDecimal parseValue(Object input) {
                BigDecimal result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'BigDecimal' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public BigDecimal parseLiteral(Object input) {
                if (input instanceof StringValue) {
                    try {
                        return new BigDecimal(((StringValue) input).getValue());
                    } catch (NumberFormatException e) {
                        throw new CoercingParseLiteralException(
                                "Unable to turn AST input into a 'BigDecimal' : '" + input + "'"
                        );
                    }
                } else if (input instanceof IntValue) {
                    return new BigDecimal(((IntValue) input).getValue());
                } else if (input instanceof FloatValue) {
                    return ((FloatValue) input).getValue();
                }
                throw new CoercingParseLiteralException(
                        "Expected AST type 'IntValue', 'StringValue' or 'FloatValue' but was '" + typeName(input) + "'."
                );
            }
        };

        GraphQLBigDecimal = GraphQLScalarType.newScalar()
                .name("BigDecimal").description("java.math.BigDecimal")
                .coercing(bigDecimalCoercing).build();
    }


    /**
     * This represents the "Char" type which is a representation of java.lang.Character
     */
    public static final GraphQLScalarType GraphQLChar;

    static {
        Coercing<Character, Character> characterCoercing = new Coercing<Character, Character>() {

            private Character convertImpl(Object input) {
                if (input instanceof String && ((String) input).length() == 1) {
                    return ((String) input).charAt(0);
                } else if (input instanceof Character) {
                    return (Character) input;
                } else {
                    return null;
                }

            }

            @Override
            public Character serialize(Object input) {
                Character result = convertImpl(input);
                if (result == null) {
                    throw new CoercingSerializeException(
                            "Expected type 'Char' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Character parseValue(Object input) {
                Character result = convertImpl(input);
                if (result == null) {
                    throw new CoercingParseValueException(
                            "Expected type 'Char' but was '" + typeName(input) + "'."
                    );
                }
                return result;
            }

            @Override
            public Character parseLiteral(Object input) {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException(
                            "Expected AST type 'StringValue' but was '" + typeName(input) + "'."
                    );
                }
                String value = ((StringValue) input).getValue();
                if (value.length() != 1) {
                    throw new CoercingParseLiteralException(
                            "Empty 'StringValue' provided."
                    );
                }
                return value.charAt(0);
            }
        };

        GraphQLChar = GraphQLScalarType.newScalar()
                .name("Char").description("Char as Character")
                .coercing(characterCoercing).build();
    }
}
