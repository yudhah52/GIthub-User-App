package com.yhezra.githubapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.yhezra.githubapp.R
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.data.remote.model.UserItem
import com.yhezra.githubapp.databinding.ActivityFavoriteBinding
import com.yhezra.githubapp.helper.ViewModelFactory
import com.yhezra.githubapp.ui.detailuser.DetailUserActivity
import com.yhezra.githubapp.ui.home.adapter.ListUserAdapter

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setToolbar()

        favoriteViewModel = obtainViewModel(this@FavoriteActivity)
        favoriteViewModel.getFavoriteUser().observe(this) { favoriteList ->
            if (favoriteList != null) {
                setFavoriteList(favoriteList)
            }
        }
    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.favorites)
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setFavoriteList(listFavorite: List<FavoriteUserEntity>) {
        val adapter = ListUserAdapter(listFavorite)

        adapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemFavoriteClicked(user: FavoriteUserEntity) {
                navigateToDetailUser(user.login)

            }

            override fun onItemUserClicked(user: UserItem) {
            }
        })

        binding.rvUserFavorite.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        binding.rvUserFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUserFavorite.addItemDecoration(itemDecoration)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun navigateToDetailUser(username: String) {
        val moveToDetailUser =
            Intent(this@FavoriteActivity, DetailUserActivity::class.java)
        moveToDetailUser.putExtra(DetailUserActivity.USERNAME, username)
        startActivity(moveToDetailUser)
    }
}