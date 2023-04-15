package com.yhezra.githubapp.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yhezra.githubapp.ui.detailuser.DetailUserViewModel
import com.yhezra.githubapp.ui.favorite.FavoriteViewModel
import com.yhezra.githubapp.ui.settings.SettingsPreferences
import com.yhezra.githubapp.ui.settings.SettingsViewModel

class ViewModelFactory(
    private val mApplication: Application?=null,
    private val pref: SettingsPreferences? = null
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(mApplication!!) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication!!) as T
        } else if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingsPreferences?=null): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}