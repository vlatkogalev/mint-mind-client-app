package collections.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import app.data.local.AppDatabase
import app.data.remote.constructUrl
import app.data.remote.safeCall
import app.domain.model.NetworkResult
import app.domain.toErrorMessage
import collections.data.local.entity.CoinEntity
import collections.data.local.entity.CoinPagingStateEntity
import collections.data.remote.dto.CoinsDto
import collections.data.remote.mapper.toCoinEntities
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter

@OptIn(ExperimentalPagingApi::class)
class CoinRemoteMediator(
    private val httpClient: HttpClient,
    private val db: AppDatabase,
    private val limit: Int,
    private val sortBy: String,
    private val queryKey: String = "$DEFAULT_QUERY_KEY:$sortBy",
) : RemoteMediator<Int, CoinEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CoinEntity>
    ): MediatorResult {
        return when (loadType) {
            LoadType.REFRESH -> loadRefresh()
            LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> loadAppend()
        }
    }

    private suspend fun loadRefresh(): MediatorResult {
        return try {
            val response = safeCall<CoinsDto> {
                httpClient.get(urlString = constructUrl("/coins")) {
                    parameter("limit", limit)
                    parameter("sortBy", sortBy)
                }
            }

            when (response) {
                is NetworkResult.Error -> {
                    MediatorResult.Error(
                        Throwable(response.error.toErrorMessage().asString())
                    )
                }

                is NetworkResult.Success -> {
                    val entities = response.data.toCoinEntities()
                    db.coinDao().replaceAll(entities)
                    db.coinPagingStateDao().clearAll()
                    db.coinPagingStateDao().upsert(
                        CoinPagingStateEntity(
                            queryKey = queryKey,
                            nextCursor = response.data.nextCursor
                        )
                    )

                    MediatorResult.Success(
                        endOfPaginationReached = response.data.nextCursor == null
                    )
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun loadAppend(): MediatorResult {
        return try {
            val pagingState = db.coinPagingStateDao().getCursor(queryKey)
            val cursor = pagingState?.nextCursor

            if (cursor == null) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = safeCall<CoinsDto> {
                httpClient.get(urlString = constructUrl("/coins")) {
                    parameter("limit", limit)
                    parameter("cursor", cursor)
                    parameter("sortBy", sortBy)
                }
            }

            when (response) {
                is NetworkResult.Error -> {
                    MediatorResult.Error(
                        Throwable(response.error.toErrorMessage().asString())
                    )
                }

                is NetworkResult.Success -> {
                    val entities = response.data.toCoinEntities()
                    db.coinDao().upsertAll(entities)
                    db.coinPagingStateDao().upsert(
                        CoinPagingStateEntity(
                            queryKey = queryKey,
                            nextCursor = response.data.nextCursor
                        )
                    )

                    MediatorResult.Success(
                        endOfPaginationReached = response.data.nextCursor == null
                    )
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        const val DEFAULT_QUERY_KEY = "all"
    }
}
