package com.yhezra.githubapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhezra.githubapp.R
import com.yhezra.githubapp.databinding.ActivityHomeBinding
import com.yhezra.githubapp.model.UserItem
import com.yhezra.githubapp.ui.detailuser.DetailUserActivity
import com.yhezra.githubapp.ui.home.adapter.ListUserAdapter

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private var isSearching = false
    private var firstVisibleItemPosition = 0
    private var lastTotalItem = 0
    private var totalItemCount = 0
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.listUser.observe(this) { listUser ->
            setUserListData(listUser)
        }
        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        binding.btnSearch.setOnClickListener(this)
    }

    private fun setUserListData(listUser: List<UserItem>) {
        val adapter = ListUserAdapter(listUser)

        adapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: UserItem) {
                navigateToDetailUser(user.login!!)
            }
        })

        binding.rvUserSearch.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserSearch.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserSearch.addItemDecoration(itemDecoration)
        binding.rvUserSearch.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                totalItemCount = layoutManager.itemCount
                Log.i("HomeActivity", "$dx $dy $lastVisibleItemPosition $totalItemCount $lastTotalItem")

                val isLoading = homeViewModel.isLoading.value ?: false
                if ((lastVisibleItemPosition == totalItemCount - 1) && (lastTotalItem!=totalItemCount) && !isLoading) {
                    if (isSearching) {
                        homeViewModel.updatePage()
                        val searchText = binding.edSearch.text.toString()
                        homeViewModel.getListUserSearch(searchText)
                    }
                    else {
                        homeViewModel.updateSince(totalItemCount+1)
                        homeViewModel.getListUser()
                    }
                    lastTotalItem = totalItemCount
                }
            }
        })
        if (lastTotalItem == totalItemCount) {
            Log.i("Followers","SCROLLPOSITION $firstVisibleItemPosition")
            layoutManager.scrollToPosition(firstVisibleItemPosition)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun navigateToDetailUser(username: String) {
        val moveToDetailUser =
            Intent(this@HomeActivity, DetailUserActivity::class.java)
        moveToDetailUser.putExtra(DetailUserActivity.USERNAME, username)
        startActivity(moveToDetailUser)
    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.btn_search -> {
                isSearching = true
                lastTotalItem = 0
                totalItemCount = 0
                firstVisibleItemPosition = 0
                homeViewModel.resetUserList()
                val searchText = binding.edSearch.text.toString()
                homeViewModel.getListUserSearch(searchText)
            }
        }
    }
}