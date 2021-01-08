package com.sideki.imagesearchapp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    var id: String,
    var description: String?,
    var urls: UnsplashPhotoUrls,
    var user: UnsplashUser
) : Parcelable {
    
    @Parcelize
    data class UnsplashPhotoUrls(
        var raw: String,
        var full: String,
        var regular: String,
        var small: String,
        var thumb: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        var name: String,
        var username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }
}
