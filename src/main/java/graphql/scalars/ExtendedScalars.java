package graphql.scalars;

import graphql.PublicApi;
import graphql.language.FloatValue;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.scalars.alias.AliasedScalar;
import graphql.scalars.datetime.DateScalar;
import graphql.scalars.datetime.DateTimeScalar;
import graphql.scalars.datetime.TimeScalar;
import graphql.scalars.numeric.NegativeFloatScalar;
import graphql.scalars.numeric.NegativeIntScalar;
import graphql.scalars.numeric.NonNegativeFloatScalar;
import graphql.scalars.numeric.NonNegativeIntScalar;
import graphql.scalars.numeric.NonPositiveFloatScalar;
import graphql.scalars.numeric.NonPositiveIntScalar;
import graphql.scalars.numeric.PositiveFloatScalar;
import graphql.scalars.numeric.PositiveIntScalar;
import graphql.scalars.object.JsonScalar;
import graphql.scalars.object.ObjectScalar;
import graphql.scalars.regex.RegexScalar;
import graphql.scalars.url.UrlScalar;
import graphql.scalars.locale.LocaleScalar;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This is the API entry point for all the extended scalars
 */
@PublicApi
public class ExtendedScalars {

    /**
     * An RFC-3339 compliant date time scalar that accepts string values like `1996-12-19T16:39:57-08:00` and produces
     * `java.time.OffsetDateTime` objects at runtime.
     * <p>
     * Its {@link graphql.schema.Coercing#serialize(java.lang.Object)} and {@link graphql.schema.Coercing#parseValue(java.lang.Object)} methods
     * accept OffsetDateTime, ZoneDateTime and formatted Strings as valid objects.
     * <p>
     * See the <a href="https://www.ietf.org/rfc/rfc3339.txt">rfc3339 spec</a> for more details on the format.
     *
     * @see java.time.OffsetDateTime
     * @see java.time.ZonedDateTime
     */
    public static GraphQLScalarType DateTime = new DateTimeScalar();

    /**
     * An RFC-3339 compliant date scalar that accepts string values like `1996-12-19` and produces
     * `java.time.LocalDate` objects at runtime.
     * <p>
     * Its {@link graphql.schema.Coercing#serialize(java.lang.Object)} and {@link graphql.schema.Coercing#parseValue(java.lang.Object)} methods
     * accept date {@link java.time.temporal.TemporalAccessor}s and formatted Strings as valid objects.
     * <p>
     * See the <a href="https://www.ietf.org/rfc/rfc3339.txt">rfc3339 spec</a> for more details on the format.
     *
     * @see java.time.LocalDate
     */
    public static GraphQLScalarType Date = new DateScalar();
    /**
     * An RFC-3339 compliant time scalar that accepts string values like `6:39:57-08:00` and produces
     * `java.time.OffsetTime` objects at runtime.
     * <p>
     * Its {@link graphql.schema.Coercing#serialize(java.lang.Object)} and {@link graphql.schema.Coercing#parseValue(java.lang.Object)} methods
     * accept time {@link java.time.temporal.TemporalAccessor}s and formatted Strings as valid objects.
     * <p>
     * See the <a href="https://www.ietf.org/rfc/rfc3339.txt">rfc3339 spec</a> for more details on the format.
     *
     * @see java.time.OffsetTime
     */
    public static GraphQLScalarType Time = new TimeScalar();

    /**
     * An object scalar allows you to have a multi level data value without defining it in the graphql schema.
     * <p>
     * It might be useful when you have opaque data coming from a backend system that you want to pass on
     * but cant provide the actual graphql schema definition for.
     * <p>
     * <b>Use this with caution</b> since is breaks one of the key benefits
     * of graphql, which is that a schema describes the shape of the data that can be queried.
     *
     * <p>
     * This can be declared as follows :
     * <pre>
     * {@code
     *
     * type Customer {
     *      name : String
     *      backendDetails : Object
     * }
     * }
     * </pre>
     *
     * @see #Json
     */
    public static GraphQLScalarType Object = new ObjectScalar();

    /**
     * A synonym class for the {@link #Object} scalar, since some people prefer their SDL to look like the following :
     *
     * <pre>
     * {@code
     *
     * type Customer {
     *      name : String
     *      backendDetails : JSON
     * }
     * }
     * </pre>
     *
     * @see graphql.scalars.ExtendedScalars#Object
     */
    public static GraphQLScalarType Json = new JsonScalar();

    /**
     * A URL scalar that accepts URL strings and produces {@link java.net.URL} objects at runtime
     */
    public static GraphQLScalarType Url = new UrlScalar();

    /**
     * A Locale scalar that accepts a IETF BCP 47 language tag string and produces {@link
     * java.util.Locale} objects at runtime.
     */
    public static GraphQLScalarType Locale = new LocaleScalar();

    /**
     * An `Int` scalar that MUST be greater than zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static GraphQLScalarType PositiveInt = new PositiveIntScalar();
    /**
     * An `Int` scalar that MUST be less than zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static GraphQLScalarType NegativeInt = new NegativeIntScalar();
    /**
     * An `Int` scalar that MUST be less than or equal to zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static GraphQLScalarType NonPositiveInt = new NonPositiveIntScalar();
    /**
     * An `Int` scalar that MUST be greater than or equal to zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static GraphQLScalarType NonNegativeInt = new NonNegativeIntScalar();

    /**
     * An `Float` scalar that MUST be greater than zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static GraphQLScalarType PositiveFloat = new PositiveFloatScalar();
    /**
     * An `Float` scalar that MUST be less than zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static GraphQLScalarType NegativeFloat = new NegativeFloatScalar();
    /**
     * An `Float` scalar that MUST be less than or equal to zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static GraphQLScalarType NonPositiveFloat = new NonPositiveFloatScalar();
    /**
     * An `Float` scalar that MUST be greater than or equal to zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static GraphQLScalarType NonNegativeFloat = new NonNegativeFloatScalar();


    /**
     * A builder of a scalar that uses one or more regular expression {@link java.util.regex.Pattern}s to control
     * the acceptable values for that scalar.
     * <p>
     * The scalar converts any passed in objects to Strings first and them matches it against the provided
     * scalars to ensure its an acceptable value.
     *
     * @return a builder of a regex scalar
     */
    public static RegexScalar.Builder newRegexScalar(String name) {
        return new RegexScalar.Builder().name(name);
    }

    /**
     * This allows an existing scalar to be wrapped and aliased with a new name.
     * <p>
     * For example you may take a `String` scalar and alias it as `SocialMediaLink` if that helps introduce
     * more semantic meaning to your type system.
     * <p>
     * <pre>
     * {@code
     *
     * type Customer {
     *      name : String
     *      socialMediaLink : SocialMediaLink
     * }
     * }
     * </pre>
     * <p>
     * A future version of the graphql specification may add this capability but in the meantime you can use this facility.
     *
     * @return a builder of a aliased scalar
     */
    public static AliasedScalar.Builder newAliasedScalar(String name) {
        return new AliasedScalar.Builder().name(name);
    }


    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
    private static final BigInteger INT_MAX = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger INT_MIN = BigInteger.valueOf(Integer.MIN_VALUE);
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
    public static final GraphQLScalarType GraphQLLong = new GraphQLScalarType("Long", "Long type", new Coercing<Long, Long>() {

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
                            "Expected value to be a Long but it was '" + String.valueOf(input) + "'"
                    );
                }
            } else if (input instanceof IntValue) {
                BigInteger value = ((IntValue) input).getValue();
                if (value.compareTo(LONG_MIN) < 0 || value.compareTo(LONG_MAX) > 0) {
                    throw new CoercingParseLiteralException(
                            "Expected value to be in the Long range but it was '" + value.toString() + "'"
                    );
                }
                return value.longValue();
            }
            throw new CoercingParseLiteralException(
                    "Expected AST type 'IntValue' or 'StringValue' but was '" + typeName(input) + "'."
            );
        }
    });

    /**
     * This represents the "Short" type which is a representation of java.lang.Short
     */
    public static final GraphQLScalarType GraphQLShort = new GraphQLScalarType("Short", "Built-in Short as Int", new Coercing<Short, Short>() {

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
                        "Expected value to be in the Short range but it was '" + value.toString() + "'"
                );
            }
            return value.shortValue();
        }
    });

    /**
     * This represents the "Byte" type which is a representation of java.lang.Byte
     */
    public static final GraphQLScalarType GraphQLByte = new GraphQLScalarType("Byte", "Built-in Byte as Int", new Coercing<Byte, Byte>() {

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
                        "Expected value to be in the Byte range but it was '" + value.toString() + "'"
                );
            }
            return value.byteValue();
        }
    });


    /**
     * This represents the "BigInteger" type which is a representation of java.math.BigInteger
     */
    public static final GraphQLScalarType GraphQLBigInteger = new GraphQLScalarType("BigInteger", "Built-in java.math.BigInteger", new Coercing<BigInteger, BigInteger>() {

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
                            "Unable to turn AST input into a 'BigInteger' : '" + String.valueOf(input) + "'"
                    );
                }
            } else if (input instanceof IntValue) {
                return ((IntValue) input).getValue();
            } else if (input instanceof FloatValue) {
                try {
                    return ((FloatValue) input).getValue().toBigIntegerExact();
                } catch (ArithmeticException e) {
                    throw new CoercingParseLiteralException(
                            "Unable to turn AST input into a 'BigInteger' : '" + String.valueOf(input) + "'"
                    );
                }
            }
            throw new CoercingParseLiteralException(
                    "Expected AST type 'IntValue', 'StringValue' or 'FloatValue' but was '" + typeName(input) + "'."
            );
        }
    });

    /**
     * This represents the "BigDecimal" type which is a representation of java.math.BigDecimal
     */
    public static final GraphQLScalarType GraphQLBigDecimal = new GraphQLScalarType("BigDecimal", "Built-in java.math.BigDecimal", new Coercing<BigDecimal, BigDecimal>() {

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
                            "Unable to turn AST input into a 'BigDecimal' : '" + String.valueOf(input) + "'"
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
    });


    /**
     * This represents the "Char" type which is a representation of java.lang.Character
     */
    public static final GraphQLScalarType GraphQLChar = new GraphQLScalarType("Char", "Built-in Char as Character", new Coercing<Character, Character>() {

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
    });
}
