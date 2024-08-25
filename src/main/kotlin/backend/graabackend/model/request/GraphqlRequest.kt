package backend.graabackend.model.request

class GraphqlRequest(
    val query: String,
    val variables: Map<String, String>
)