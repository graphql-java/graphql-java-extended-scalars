package graphql.scalars.object;

import graphql.Internal;

/**
 * A synonym class for {@link graphql.scalars.object.ObjectScalar}
 *
 * Access this via {@link graphql.scalars.ExtendedScalars#Json}
 */
@Internal
public class JsonScalar extends ObjectScalar {
    public JsonScalar() {
        super("JSON", "A JSON scalar");
    }
}
