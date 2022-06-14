package com.example.githubedwin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users (
    var userName: String?,
    var company: String?,
    var location: String?,
    var avatar: Int,
    var name: String?,
    var repository: Int?,
    var follower: Int?,
    var following: Int?,
) : Parcelable

