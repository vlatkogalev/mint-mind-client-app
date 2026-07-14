package collections.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BulkDeleteCoinsRequest(val coinIds: List<String>)

@Serializable
data class BulkDeleteSetsRequest(val setIds: List<String>)

@Serializable
data class ModifySetCoinsRequest(val coinIds: List<String>)

@Serializable
data class BulkDeleteResponse(val requested: Int, val deleted: Int)
