package graphql.scalars;

import graphql.PublicApi;
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
}
