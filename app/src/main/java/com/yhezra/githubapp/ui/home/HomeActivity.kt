package com.yhezra.githubapp.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhezra.githubapp.R
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.databinding.ActivityHomeBinding
import com.yhezra.githubapp.data.remote.model.UserItem
import com.yhezra.githubapp.ui.detailuser.DetailUserActivity
import com.yhezra.githubapp.ui.favorite.FavoriteActivity
import com.yhezra.githubapp.ui.home.adapter.ListUserAdapter
import com.yhezra.githubapp.ui.settings.SettingsActivity

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
            override fun onItemFavoriteClicked(item: FavoriteUserEntity) {

            }

            override fun onItemUserClicked(user: UserItem) {
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

                val isLoading = homeViewModel.isLoading.value ?: false
                if ((lastVisibleItemPosition == totalItemCount - 1) && (lastTotalItem != totalItemCount) && !isLoading) {
                    if (isSearching) {
                        homeViewModel.updatePage()
                        val searchText = binding.edSearch.text.toString()
                        homeViewModel.getListUserSearch(searchText)
                    } else {
                        homeViewModel.updateSince(totalItemCount + 1)
                        homeViewModel.getListUser()
                    }
                    lastTotalItem = totalItemCount
                }
            }
        })
        if (lastTotalItem == totalItemCount) {
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

    private fun navigateToFavorite() {
        val moveToFavorite =
            Intent(this@HomeActivity, FavoriteActivity::class.java)
        startActivity(moveToFavorite)
    }

    private fun navigateToSettings() {
        val moveToSettings =
            Intent(this@HomeActivity, SettingsActivity::class.java)
        startActivity(moveToSettings)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> navigateToFavorite()
            R.id.action_settings -> navigateToSettings()
        }
        return super.onOptionsItemSelected(item)
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