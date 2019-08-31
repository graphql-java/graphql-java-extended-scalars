package graphql.scalars.locale;

import static graphql.scalars.util.Kit.typeName;

import graphql.Internal;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;
import java.util.Locale;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#Locale}
 */
@Internal
public class LocaleScalar extends GraphQLScalarType {

  public LocaleScalar() {
    super("Locale", "A IETF BCP 47 language tag", new Coercing<Locale, String>() {

      @Override
      public String serialize(Object input) throws CoercingSerializeException {
        if (input instanceof String) {
          try {
            return Locale.forLanguageTag((String) input).toLanguageTag();
          } catch (Exception e) {
            throw new CoercingSerializeException(
                "Expected a valid language tag string but was but was " + typeName(input));
          }
        }
        if (input instanceof Locale) {
          return ((Locale) input).toLanguageTag();
        } else {
          throw new CoercingSerializeException(
              "Expected a 'java.util.Locale' object but was " + typeName(input));
        }
      }

      @Override
      public Locale parseValue(Object input) throws CoercingParseValueException {
        if (input instanceof String) {
          try {
            return Locale.forLanguageTag(input.toString());
          } catch (Exception e) {
            throw new CoercingParseValueException(
                "Unable to parse value to 'java.util.Locale' because of: " + e.getMessage());
          }
        } else {
          throw new CoercingParseValueException(
              "Expected a 'java.lang.String' object but was " + typeName(input));
        }
      }

      @Override
      public Locale parseLiteral(Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue) {
          return Locale.forLanguageTag(((StringValue) input).getValue());
        } else {
          throw new CoercingParseLiteralException(
              "Expected a 'java.lang.String' object but was " + typeName(input));
        }
      }
    });
  }
}
