package graphql.scalars.object

import graphql.ExecutionInput
import graphql.GraphQL
import graphql.scalars.ExtendedScalars
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeRuntimeWiring
import spock.lang.Specification
import spock.lang.Unroll

class JsonScalarTest extends Specification {

    GraphQL graphQL

    def setup() {
        def typeRegistry = new SchemaParser().parse('''
            scalar JSON
            type Query {
                echo(value: JSON): JSON
            }
        ''')
        def wiring = RuntimeWiring.newRuntimeWiring()
                .scalar(ExtendedScalars.Json)
                .type(TypeRuntimeWiring.newTypeWiring("Query")
                        .dataFetcher("echo", { env -> env.getArgument("value") }))
                .build()
        def schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring)
        graphQL = GraphQL.newGraphQL(schema).build()
    }

    @Unroll
    def "accepts null values in JSON literal #literal"() {
        when:
        def result = graphQL.execute("{ echo(value: $literal) }")

        then:
        result.errors.empty
        result.getData() == [echo: expected]

        where:
        literal                           | expected
        '[null]'                          | [null]
        '[1, null, 2]'                     | [1, null, 2]
        '[null, 1, null]'                  | [null, 1, null]
        '[[null]]'                        | [[null]]
        '{items: [null, 1], value: null}'   | [items: [null, 1], value: null]
        '[{value: null}]'                  | [[value: null]]
        'null'                            | null
    }

    def "accepts null list elements in JSON variables"() {
        given:
        def value = [items: [1, null, [null]], value: null]

        when:
        def result = graphQL.execute(ExecutionInput.newExecutionInput()
                .query('query($value: JSON) { echo(value: $value) }')
                .variables([value: value])
                .build())

        then:
        result.errors.empty
        result.getData() == [echo: value]
    }
}
