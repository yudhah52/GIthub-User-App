package com.yhezra.githubapp.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yhezra.githubapp.api.ApiConfig
import com.yhezra.githubapp.model.SearchUsersResponse
import com.yhezra.githubapp.model.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _listUser = MutableLiveData<List<UserItem>>()
    val listUser: LiveData<List<UserItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var _since = 0
    private var _page = 1
    private val _perPage = 40

    companion object {
        private const val TAG = "HomeViewModel"
    }

    init {
        getListUser()
    }

    fun getListUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListUser(_since, _perPage)
        client.enqueue(object : Callback<List<UserItem>> {
            override fun onResponse(
                call: Call<List<UserItem>>,
                response: Response<List<UserItem>>
            ) {
//                Log.i(TAG, "listuser ${response.body()}")
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val newUserList = _listUser.value?.plus(responseBody) ?: responseBody
                        _listUser.postValue(newUserList)
                        Log.i(TAG, "$_page $_perPage ${listUser.value?.size} ${responseBody.size} } listUser")
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

    fun getListUserSearch(searchText: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(searchText,_page,_perPage)
        client.enqueue(object : Callback<SearchUsersResponse> {
            override fun onResponse(
                call: Call<SearchUsersResponse>,
                response: Response<SearchUsersResponse>
            ) {
//                Log.i(TAG, "listusersearch ${response.body()?.items}")
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val newUserList = _listUser.value?.plus(responseBody.items) ?: responseBody
                        _listUser.postValue(newUserList as List<UserItem>?)

                        Log.i(TAG, "$_page $_perPage ${listUser.value?.size} ${responseBody.items.size} } listUserSearch")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<SearchUsersResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun updateSince(since:Int){
        _since = since
    }

    fun updatePage(){
        _page++
    }

    fun resetUserList() {
        _listUser.value = emptyList()
        _page = 1
    }
}