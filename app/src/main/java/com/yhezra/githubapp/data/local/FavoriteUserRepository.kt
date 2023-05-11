package com.yhezra.githubapp.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.data.local.room.FavoriteUserDao
import com.yhezra.githubapp.data.local.room.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavoriteUserDao = db.favoriteUserDao()
    }

    interface OpertaionFavoriteCallback {
        fun onOperationFavoriteComplete(user:FavoriteUserEntity)
    }

    fun insertFavorite(user: FavoriteUserEntity, callback: OpertaionFavoriteCallback) {
        executorService.execute {
            mFavoriteUserDao.insertFavorite(user)
            callback.onOperationFavoriteComplete(user)
        }
    }

    fun deleteFavorite(user: FavoriteUserEntity, callback: OpertaionFavoriteCallback) {
        executorService.execute {
            mFavoriteUserDao.deleteFavorite(user)
            callback.onOperationFavoriteComplete(user)
        }
    }

    fun isFavoritedUser(username: String): Boolean =
        mFavoriteUserDao.isFavoritedUser(username)

    fun getFavoriteUser(): LiveData<List<FavoriteUserEntity>> = mFavoriteUserDao.getFavoriteUser()
}