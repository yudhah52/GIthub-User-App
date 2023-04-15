package com.yhezra.githubapp.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.yhezra.githubapp.data.local.FavoriteUserRepository
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    fun getFavoriteUser(): LiveData<List<FavoriteUserEntity>> =
        mFavoriteUserRepository.getFavoriteUser()

}