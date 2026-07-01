package collections.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.model.NetworkResult
import app.domain.toErrorMessage
import collections.data.remote.dto.CoinsDto
import collections.data.remote.mapper.toCoin
import collections.domain.model.Coin
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class CoinPagingSource(
    private val httpClient: HttpClient,
    private val limit: Int
) : PagingSource<Long, Coin>() {

    override fun getRefreshKey(state: PagingState<Long, Coin>): Long? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition)
        return anchorPage?.prevKey ?: anchorPage?.nextKey
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Coin> {
        val cursor = params.key

        return try {
            when (
                val response = safeCall<CoinsDto> {
                    httpClient.get(urlString = constructUrl("/coins")) {
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
                    val items = payload.toCoin()

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