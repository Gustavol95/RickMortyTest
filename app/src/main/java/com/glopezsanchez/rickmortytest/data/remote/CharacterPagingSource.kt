package com.glopezsanchez.rickmortytest.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CharacterPagingSource(
    private val service: ApiService,
    private val filter: String?
) : PagingSource<Int, CharacterDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getCharacterData(position, filter)
            val items = response.results
            val nextKey = if (response.info.next == null)  {
                null
            } else {
                position + 1
            }
            LoadResult.Page(
                data = items,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}