package com.yhezra.githubapp.ui.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yhezra.githubapp.R
import com.yhezra.githubapp.databinding.ActivityDetailUserBinding
import com.yhezra.githubapp.model.DetailUser
import com.yhezra.githubapp.ui.detailuser.adapter.SectionPagerAdapter

class DetailUserActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val USERNAME = "username"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private var username: String? = null
    private val detailUserViewModel: DetailUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(USERNAME)

        setToolbar()

        detailUserViewModel.getDetailUser(username!!)
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }
        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setTabs()
    }

    private fun setToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.detail_user)
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setTabs() {
        val sectionsPagerAdapter = SectionPagerAdapter(this)
        sectionsPagerAdapter.username = username

        binding.viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailUser(detailUser: DetailUser) {
        Glide.with(this)
            .load(detailUser.avatarUrl) // URL Gambar
            .into(binding.imgPhoto) // im
        binding.tvName.text = detailUser.name
        binding.tvUsername.text = detailUser.login
        binding.tvFollowers.text = detailUser.followers.toString()
        binding.tvFollowing.text = detailUser.following.toString()
        binding.tvRepository.text = detailUser.publicRepos.toString()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}