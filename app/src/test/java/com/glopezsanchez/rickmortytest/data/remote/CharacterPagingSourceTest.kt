package com.gus.testrick.data.remote

import androidx.paging.PagingSource
import com.glopezsanchez.rickmortytest.data.remote.ApiService
import com.glopezsanchez.rickmortytest.data.remote.CharacterPagingSource
import com.glopezsanchez.rickmortytest.data.remote.PageInfoDto
import com.glopezsanchez.rickmortytest.data.remote.PagedCharacterListResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class CharacterPagingSourceTest {


    private val mockApi = mockk<ApiService>()

    @Test
    fun `Emit LoadResult Page when api call is successful`() = runTest {
        coEvery { mockApi.getCharacterData(1, null) } returns getFakeResponse()
        val pagingSource = CharacterPagingSource(mockApi, null)

        // input
        val refreshLoadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        val result = pagingSource.load(refreshLoadParams)

        val expected = PagingSource.LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = 2
        )

        assertTrue(result is PagingSource.LoadResult.Page)
        assertEquals(expected.prevKey, (result as PagingSource.LoadResult.Page).prevKey)
        assertEquals(expected.nextKey, result.nextKey)
        assertEquals(expected.data, result.data)
    }

    @Test
    fun `Emit LoadResult Error when api call fails`() = runTest {
        coEvery { mockApi.getCharacterData(1, null) }.throws(getHttpException())
        val pagingSource = CharacterPagingSource(mockApi, null)
        val refreshLoadParams = PagingSource.LoadParams.Refresh<Int>(
            key = null,
            loadSize = 20,
            placeholdersEnabled = false
        )

        val result = pagingSource.load(refreshLoadParams)

        assertTrue(result is PagingSource.LoadResult.Error)
        assertTrue((result as PagingSource.LoadResult.Error).throwable is HttpException)
    }


    private fun getFakeResponse(): PagedCharacterListResponse {
        return PagedCharacterListResponse(
            info = PageInfoDto(
                count = 20,
                pages = 23,
                next = "Not null",
                prev = null
            ),
            results = listOf()
        )
    }

    private fun getHttpException(): HttpException {
        return HttpException(
            Response.error<PagedCharacterListResponse>(
                500,
                "{}".toResponseBody()
            )
        )
    }

}