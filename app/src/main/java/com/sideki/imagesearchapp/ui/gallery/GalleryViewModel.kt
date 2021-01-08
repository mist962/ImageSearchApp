package com.sideki.imagesearchapp.ui.gallery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.sideki.imagesearchapp.data.UnsplashRepository

/**
 * 1) После создания [UnsplashPaggingSource] и провода его в [UnsplashRepository] создаем -
 * [companion_object], переменные [currentQuery] и [photos] и функцию [searchPhotos]
 */

class GalleryViewModel @ViewModelInject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    companion object {
        private const val DEFAULT_QUERY = "cats"
    }

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope) //Страница будет кешироваться при повороте
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }
}