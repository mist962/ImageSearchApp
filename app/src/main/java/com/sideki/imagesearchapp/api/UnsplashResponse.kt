package com.sideki.imagesearchapp.api

import com.sideki.imagesearchapp.data.UnsplashPhoto

data class UnsplashResponse(
    val results: List<UnsplashPhoto>
)