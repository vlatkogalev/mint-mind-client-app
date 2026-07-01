package feed.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.model.NetworkResult
import app.domain.toErrorMessage
import feed.data.remote.dto.MarketplaceListingsDto
import feed.data.remote.mapper.toCoinListing
import feed.domain.model.CoinListing
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CoinListingsPagingSource(
    private val httpClient: HttpClient,
    private val limit: Int
) : PagingSource<Long, CoinListing>() {

    override fun getRefreshKey(state: PagingState<Long, CoinListing>): Long? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition)
        return anchorPage?.prevKey ?: anchorPage?.nextKey
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CoinListing> {
        val cursor = params.key

        return try {
            when (
                val response = safeCall<MarketplaceListingsDto> {
                    httpClient.get(urlString = constructUrl("/marketplace/listings")) {
                        parameter("limit", limit)
                        if (cursor != null) {
                            parameter("cursor", cursor)
                        }
                    }
                }
            ) {
                is NetworkResult.Error -> {
                    LoadResult.Error(Throwable(response.error.toErrorMessage().asString()))
                }

                is NetworkResult.Success -> {
                    val payload = response.data
                    val items = payload.toCoinListing()

                    LoadResult.Page(
                        data = items,
                        prevKey = null,
                        nextKey = payload.nextCursor
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}