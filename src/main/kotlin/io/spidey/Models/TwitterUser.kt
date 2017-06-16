package io.spidey.Models

data class TwitterUser(
    val userName: String,
    val description: String,
    val profilePictureUrl: String,
    val bannerPictureUrl: String?
)