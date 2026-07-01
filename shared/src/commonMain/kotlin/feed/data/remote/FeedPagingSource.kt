package feed.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.model.NetworkResult
import app.domain.toErrorMessage
import feed.data.remote.dto.PostsDto
import feed.data.remote.mapper.toPosts
import feed.domain.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class FeedPagingSource(
    private val httpClient: HttpClient,
    private val limit: Int
) : PagingSource<Long, Post>() {

    override fun getRefreshKey(state: PagingState<Long, Post>): Long? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition)
        return anchorPage?.prevKey ?: anchorPage?.nextKey
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, Post> {
        val cursor = params.key

        return try {
            when (
                val response = safeCall<PostsDto> {
                    httpClient.get(urlString = constructUrl("/news")) {
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
                    val items = payload.toPosts()

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