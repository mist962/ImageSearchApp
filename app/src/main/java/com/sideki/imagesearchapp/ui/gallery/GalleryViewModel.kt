package com.sideki.imagesearchapp.ui.gallery

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sideki.imagesearchapp.data.UnsplashPhoto
import com.sideki.imagesearchapp.data.UnsplashRepository
import kotlinx.coroutines.launch

/**
 * 1) После создания [UnsplashPaggingSource] и провода его в [UnsplashRepository] создаем -
 * [companion_object], переменные [currentQuery] и [photos] и функцию [searchPhotos]
 * private val currentQuery = MutableLiveData(DEFAULT_QUERY)
 *
 * ----------Сохранение состояния--------------
 * 2) Добавляем в коеструктор @Assisted state: SavedStateHandle
 * 3) Заменяем переменную currentQuery
 * 4) Добавляем private const val CURRENT_QUERY = "dogs"
 */

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val CURRENT_QUERY = "dogs"
        private const val DEFAULT_QUERY = "cats"
    }

     private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope) //Страница будет кешироваться при повороте
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }
}