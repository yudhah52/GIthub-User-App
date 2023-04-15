package com.yhezra.githubapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

//@Entity(tableName = "favorite_user")
//class FavoriteUserEntity (
//    @field:PrimaryKey
//    @field:ColumnInfo(name = "login")
//    val login: String,
//
//    @field:ColumnInfo(name = "avatar_url")
//    val avatarUrl: String,
//)

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUserEntity(
    @PrimaryKey
    @ColumnInfo(name = "login")
    var login: String = "",

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String = "",

) : Parcelable