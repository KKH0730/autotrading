package st.seno.autotrading.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import st.seno.autotrading.data.network.model.ClosedOrder

class ItemPagingSource : PagingSource<Int, ClosedOrder>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClosedOrder> {
        return try {
            val currentPage = params.key ?: 1
            val pageSize = params.loadSize
            val items = loadItemsFromServer(currentPage, pageSize) // 서버에서 데이터 로드
            LoadResult.Page(
                data = items,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (items.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ClosedOrder>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    private suspend fun loadItemsFromServer(page: Int, size: Int): List<ClosedOrder> {
        // 예제 데이터 (서버에서 받아온다고 가정)
//        return List(size) { index -> ClosedOrder(id = (page - 1) * size + index, name = "Item ${(page - 1) * size + index}") }
        return listOf()
    }
}