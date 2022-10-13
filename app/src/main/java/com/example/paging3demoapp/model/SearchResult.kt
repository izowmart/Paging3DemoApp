package com.example.paging3demoapp.model

import androidx.room.Embedded
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("results")
    @Embedded
    val images: List<UnsplashedImage>
)