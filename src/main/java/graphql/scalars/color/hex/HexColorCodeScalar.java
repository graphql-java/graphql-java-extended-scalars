package graphql.scalars.color.hex;

import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.awt.*;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Pattern;

import static graphql.scalars.util.Kit.typeName;

/**
 * Access this via {@link graphql.scalars.ExtendedScalars#HexColorCode}
 * See the <a href="https://en.wikipedia.org/wiki/Web_colors">Web colors</a> for more details.
 * <p>
 * Supports the following formats: #RGB, #RGBA, #RRGGBB, #RRGGBBAA. Need to be prefixed with '#'
 */
public class HexColorCodeScalar {

    public static final GraphQLScalarType INSTANCE;


    static {
        Coercing<Color, String> coercing = new Coercing<>() {

            private final Pattern HEX_PATTERN = Pattern.compile("^(#([A-Fa-f0-9]{3,4}){1,2})$");

            @Override
            public String serialize(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingSerializeException {
                Color color = parseColor(input, CoercingSerializeException::new);
                boolean hasAlpha = color.getAlpha() != 255;
                if (hasAlpha) {
                    return String.format("#%02x%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
                } else {
                    return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
                }
            }

            @Override
            public Color parseValue(Object input, GraphQLContext graphQLContext, Locale locale) throws CoercingParseValueException {
                return parseColor(input, CoercingParseValueException::new);
            }

            @Override
            public Color parseLiteral(Value<?> input, CoercedVariables variables, GraphQLContext graphQLContext, Locale locale) throws CoercingParseLiteralException {
                if (!(input instanceof StringValue)) {
                    throw new CoercingParseLiteralException("Expected type 'StringValue' but was '" + typeName(input) + "'.");
                }
                String stringValue = ((StringValue) input).getValue();
                return parseColor(stringValue, CoercingParseLiteralException::new);
            }

            @Override
            public Value<?> valueToLiteral(Object input, GraphQLContext graphQLContext, Locale locale) {
                String s = serialize(input, graphQLContext, locale);
                return StringValue.newStringValue(s).build();
            }


            private Color parseColor(Object input, Function<String, RuntimeException> exceptionMaker) {
                final Color result;
                if (input instanceof Color) {
                    result = (Color) input;
                } else if (input instanceof String) {
                    try {
                        String hex = ((String) input);

                        //validation
                        //regex
                        if (!HEX_PATTERN.matcher(hex).matches()) {
                            throw new IllegalArgumentException("Invalid hex color code value : '" + input + "'.");
                        }

                        int i = Integer.decode(hex);
                        int inputLength = hex.length();

                        if (inputLength == 4) {
                            // #RGB
                            result = new Color(
                                    (i >> 8 & 0xF) * 0x11,
                                    (i >> 4 & 0xF) * 0x11,
                                    (i & 0xF) * 0x11
                            );
                        } else if (inputLength == 5) {
                            // #RGBA
                            result = new Color(
                                    (i >> 12 & 0xF) * 0x11,
                                    (i >> 8 & 0xF) * 0x11,
                                    (i >> 4 & 0xF) * 0x11,
                                    (i & 0xF) * 0x11
                            );
                        } else if (inputLength == 7) {
                            // #RRGGBB
                            result = new Color(i);
                        } else {
                            // #RRGGBBAA
                            result = new Color(
                                    (i >> 24 & 0xFF),
                                    (i >> 16 & 0xFF),
                                    (i >> 8 & 0xFF),
                                    (i & 0xFF)
                            );
                        }
                    } catch (NullPointerException | IllegalArgumentException ex) {
                        throw exceptionMaker.apply("Invalid hex color code value : '" + input + "'. because of : '" + ex.getMessage() + "'");
                    }
                } else {
                    throw exceptionMaker.apply("Expected a 'String' or 'Color' but was '" + typeName(input) + "'.");
                }
                return result;
            }

        };

        INSTANCE = GraphQLScalarType.newScalar()
                .name("HexColorCode")
                .description("A field whose value is a hexadecimal color code: https://en.wikipedia.org/wiki/Web_colors.")
                .coercing(coercing).build();
    }

}
