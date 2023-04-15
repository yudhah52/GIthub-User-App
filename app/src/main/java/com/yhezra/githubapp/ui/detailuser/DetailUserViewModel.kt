package com.yhezra.githubapp.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yhezra.githubapp.data.local.FavoriteUserRepository
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.data.remote.api.ApiConfig
import com.yhezra.githubapp.data.remote.model.DetailUser
import com.yhezra.githubapp.data.remote.model.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : ViewModel(),
    FavoriteUserRepository.OpertaionFavoriteCallback {

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailUser = MutableLiveData<DetailUser>()
    val detailUser: LiveData<DetailUser> = _detailUser

    private val _listFollowers = MutableLiveData<List<UserItem>>()
    val listFollowers: LiveData<List<UserItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<UserItem>>()
    val listFollowing: LiveData<List<UserItem>> = _listFollowing

    private val _isFavorited = MutableLiveData<Boolean>()
    val isFavorited: LiveData<Boolean> = _isFavorited

    private var _page = 1
    private val _perPage = 40

    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUser> {
            override fun onResponse(
                call: Call<DetailUser>,
                response: Response<DetailUser>
            ) {
                Log.i(TAG, "detailuser ${response.body()}")

                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUser.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUser>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListFollowingUser(username, _page, _perPage)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
//                Log.i(TAG,"listfollowing ${response.body()}")

                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val newFollowList = _listFollowing.value?.plus(responseBody) ?: responseBody
                        _listFollowing.postValue(newFollowList)

                        Log.i(
                            TAG,
                            "$_page $_perPage ${listFollowing.value?.size} ${responseBody.size} } listFollowing"
                        )
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListFollowersUser(username, _page, _perPage)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
//                Log.i(TAG,"listfollowers ${response.body()}")

                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val newFollowList = _listFollowers.value?.plus(responseBody) ?: responseBody
                        _listFollowers.postValue(newFollowList)

                        Log.i(
                            TAG,
                            "$_page $_perPage ${listFollowers.value?.size} ${responseBody.size} } listFollowers"
                        )
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun updatePage() {
        _page++
    }

    //FAVORITE

    fun updateLoading() {
        _isLoading.value = !_isLoading.value!!
    }

    fun insertFavorite(user: FavoriteUserEntity) {
        mFavoriteUserRepository.insertFavorite(user, this)
    }

    fun deleteFavorite(user: FavoriteUserEntity) {
        mFavoriteUserRepository.deleteFavorite(user, this)
    }

    override fun onOperationFavoriteComplete(user: FavoriteUserEntity) {
        isFavoritedUser(username = user.login)
    }

    fun isFavoritedUser(username: String) {
        _isFavorited.postValue(mFavoriteUserRepository.isFavoritedUser(username))
    }

}