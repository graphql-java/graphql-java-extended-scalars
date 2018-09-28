package graphql.scalars.util;

public class Kit {

    public static String typeName(Object input) {
        if (input == null) {
            return "null";
        }
        return input.getClass().getSimpleName();
    }

}
