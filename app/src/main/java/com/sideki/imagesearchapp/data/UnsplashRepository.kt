package com.sideki.imagesearchapp.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.sideki.imagesearchapp.api.UnsplashApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 1) Функцию [getSearchResults] создаем после создания [UnsplashPaggingSource]
 * 2) В параметре передаем [query] - поисковый запрос на картинки
 * 3) В функции getSearchResults создаем [Pager] и передаем в него 2 параметра: [PagingConfig] и [pagingSourceFactory]
 * 4) [pageSize] - будет добавлено в [params.loadSize] в [UnsplashPaggingSourrce]
 * 5) [maxSize] - количество кешируемых объектов, если указать слишком много, памяти может не хватить
 * 6) [enablePlaceholders] - если true, то при первом открытии загрузит большое количество объектов, больше [pageSize]
 * 7) В параметр [pagingSourceFactory] - передаем [UnsplashApi] и [query]
 * 8) Приводим все к [liveData]
 * 9) Вызываем метод [getSearchResults] в [GalleryViewModel]
 */

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi) {

    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                UnsplashPaggingSource(
                    unsplashApi,
                    query
                )
            }).liveData
}