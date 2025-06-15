package graphql.scalars;

import graphql.PublicApi;
import graphql.scalars.alias.AliasedScalar;
import graphql.scalars.color.hex.HexColorCodeScalar;
import graphql.scalars.country.code.CountryCodeScalar;
import graphql.scalars.currency.CurrencyScalar;
import graphql.scalars.datetime.DateScalar;
import graphql.scalars.datetime.DateTimeScalar;
import graphql.scalars.datetime.AccurateDurationScalar;
import graphql.scalars.datetime.LocalTimeCoercing;
import graphql.scalars.datetime.NominalDurationScalar;
import graphql.scalars.datetime.TimeScalar;
import graphql.scalars.java.JavaPrimitives;
import graphql.scalars.locale.LocaleScalar;
import graphql.scalars.id.UUIDScalar;
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
import graphql.schema.GraphQLScalarType;

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
    public static final GraphQLScalarType DateTime = DateTimeScalar.INSTANCE;

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
    public static final GraphQLScalarType Date = DateScalar.INSTANCE;
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
    public static final GraphQLScalarType Time = TimeScalar.INSTANCE;

    /**
     * A 24-hour local time scalar that accepts strings like `hh:mm:ss` and `hh:mm:ss.sss` and produces
     * `java.time.LocalTime` objects at runtime.
     * <p>
     * Its {@link graphql.schema.Coercing#serialize(java.lang.Object)} and {@link graphql.schema.Coercing#parseValue(java.lang.Object)} methods
     * accept time {@link java.time.temporal.TemporalAccessor}s and formatted Strings as valid objects.
     *
     * @see java.time.LocalTime
     */
    public static final GraphQLScalarType LocalTime = GraphQLScalarType.newScalar()
            .name("LocalTime")
            .description("24-hour clock time value string in the format `hh:mm:ss` or `hh:mm:ss.sss`.")
            .coercing(new LocalTimeCoercing())
            .build();

    /**
     * A duration scalar that accepts string values like `P1DT2H3M4.5s` and produces * `java.time.Duration` objects at runtime.
     * <p>
     * Components like years and months are not supported as these may have different meanings depending on the placement in the calendar year.
     * <p>
     * Its {@link graphql.schema.Coercing#serialize(java.lang.Object)} and {@link graphql.schema.Coercing#parseValue(java.lang.Object)} methods
     * accept Duration and formatted Strings as valid objects.
     * <p>
     * See the ISO 8601 for more details on the format.
     *
     * @see java.time.Duration
     */
    public static final GraphQLScalarType AccurateDuration = AccurateDurationScalar.INSTANCE;

    /**
     * An RFC-3339 compliant duration scalar that accepts string values like `P1Y2M3D` and produces
     * `java.time.Period` objects at runtime.
     * <p>
     * Components like hours and seconds are not supported as these are handled by {@link #AccurateDuration}.
     * <p>
     * Its {@link graphql.schema.Coercing#serialize(java.lang.Object)} and {@link graphql.schema.Coercing#parseValue(java.lang.Object)} methods
     * accept Period and formatted Strings as valid objects.
     * <p>
     * See the ISO 8601 for more details on the format.
     *
     * @see java.time.Period
     */
    public static final GraphQLScalarType NominalDuration = NominalDurationScalar.INSTANCE;

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
    public static final GraphQLScalarType Object = ObjectScalar.INSTANCE;

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
    public static final GraphQLScalarType Json = JsonScalar.INSTANCE;

    /**
     * A URL scalar that accepts URL strings and produces {@link java.net.URL} objects at runtime
     */
    public static final GraphQLScalarType Url = UrlScalar.INSTANCE;

    /**
     * A Locale scalar that accepts a IETF BCP 47 language tag string and produces {@link
     * java.util.Locale} objects at runtime.
     */
    public static final GraphQLScalarType Locale = LocaleScalar.INSTANCE;

    /**
     * A field whose value is an ISO-4217 currency.
     * See the <a href="https://en.wikipedia.org/wiki/ISO_4217">ISO-4217</a> for more details.
     */
    public static final GraphQLScalarType Currency = CurrencyScalar.INSTANCE;

    /**
     * The CountryCode scalar type as defined by ISO 3166-1 alpha-2.
     * See the <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO 3166-1 alpha-2</a> for more details.
     */
    public static final GraphQLScalarType CountryCode = CountryCodeScalar.INSTANCE;

    /**
     * A field whose value is a hex color code
     * See the <a href="https://en.wikipedia.org/wiki/Web_colors">Web colors</a> for more details.
     */
    public static final GraphQLScalarType HexColorCode = HexColorCodeScalar.INSTANCE;

    /**
     * A UUID scalar that accepts a universally unique identifier and produces {@link
     * java.util.UUID} objects at runtime.
     */
    public static final GraphQLScalarType UUID = UUIDScalar.INSTANCE;

    /**
     * An `Int` scalar that MUST be greater than zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static final GraphQLScalarType PositiveInt = PositiveIntScalar.INSTANCE;
    /**
     * An `Int` scalar that MUST be less than zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static final GraphQLScalarType NegativeInt = NegativeIntScalar.INSTANCE;
    /**
     * An `Int` scalar that MUST be less than or equal to zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static final GraphQLScalarType NonPositiveInt = NonPositiveIntScalar.INSTANCE;
    /**
     * An `Int` scalar that MUST be greater than or equal to zero
     *
     * @see graphql.Scalars#GraphQLInt
     */
    public static final GraphQLScalarType NonNegativeInt = NonNegativeIntScalar.INSTANCE;

    /**
     * An `Float` scalar that MUST be greater than zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static final GraphQLScalarType PositiveFloat = PositiveFloatScalar.INSTANCE;
    /**
     * An `Float` scalar that MUST be less than zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static final GraphQLScalarType NegativeFloat = NegativeFloatScalar.INSTANCE;
    /**
     * An `Float` scalar that MUST be less than or equal to zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static final GraphQLScalarType NonPositiveFloat = NonPositiveFloatScalar.INSTANCE;
    /**
     * An `Float` scalar that MUST be greater than or equal to zero
     *
     * @see graphql.Scalars#GraphQLFloat
     */
    public static final GraphQLScalarType NonNegativeFloat = NonNegativeFloatScalar.INSTANCE;


    /**
     * A builder of a scalar that uses one or more regular expression {@link java.util.regex.Pattern}s to control
     * the acceptable values for that scalar.
     * <p>
     * The scalar converts any passed in objects to Strings first and them matches it against the provided
     * scalars to ensure its an acceptable value.
     *
     * @param name the name of the scalar
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
     * @param name the name of the aliased scalar
     *
     * @return a builder of a aliased scalar
     */
    public static AliasedScalar.Builder newAliasedScalar(String name) {
        return new AliasedScalar.Builder().name(name);
    }

    /**
     * This represents the "Long" type which is a representation of java.lang.Long
     */
    public static final GraphQLScalarType GraphQLLong = JavaPrimitives.GraphQLLong;

    /**
     * This represents the "Short" type which is a representation of java.lang.Short
     */
    public static final GraphQLScalarType GraphQLShort = JavaPrimitives.GraphQLShort;

    /**
     * This represents the "Byte" type which is a representation of java.lang.Byte
     */
    public static final GraphQLScalarType GraphQLByte = JavaPrimitives.GraphQLByte;

    /**
     * This represents the "BigDecimal" type which is a representation of java.math.BigDecimal
     */
    public static final GraphQLScalarType GraphQLBigDecimal = JavaPrimitives.GraphQLBigDecimal;

    /**
     * This represents the "BigInteger" type which is a representation of java.math.BigInteger
     */
    public static final GraphQLScalarType GraphQLBigInteger = JavaPrimitives.GraphQLBigInteger;

    /**
     * This represents the "Char" type which is a representation of java.lang.Character
     */
    public static final GraphQLScalarType GraphQLChar = JavaPrimitives.GraphQLChar;

}
