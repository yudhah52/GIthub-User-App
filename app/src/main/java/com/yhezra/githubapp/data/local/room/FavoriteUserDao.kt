package com.yhezra.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: FavoriteUserEntity)

    @Delete
    fun deleteFavorite(user: FavoriteUserEntity)

    @Query("SELECT* FROM favorite_user ORDER by login ASC")
    fun getFavoriteUser(): LiveData<List<FavoriteUserEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_user WHERE login = :username)")
    fun isFavoritedUser(username: String): Boolean
}