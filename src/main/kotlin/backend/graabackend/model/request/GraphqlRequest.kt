package backend.graabackend.model.request

class GraphqlRequest(
    val query: String,
    val variables: Map<String, String>
)

class NftItemsByOwnerRequest(
    val query: String,
    val variables: Variables
)

class Variables(
    val first: Int,
    val ownerAddress: String
)