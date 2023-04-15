package com.yhezra.githubapp.ui.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.yhezra.githubapp.R
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.databinding.ActivityDetailUserBinding
import com.yhezra.githubapp.data.remote.model.DetailUser
import com.yhezra.githubapp.helper.ViewModelFactory
import com.yhezra.githubapp.ui.detailuser.adapter.SectionPagerAdapter
import kotlinx.coroutines.launch

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val USERNAME = "username"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    //    private val detailUserViewModel: DetailUserViewModel by viewModels()

    private var username: String? = null
    private var detailUser: DetailUser? = null
    private var favoriteUserEntity: FavoriteUserEntity? = null
    private var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(USERNAME)
        detailUserViewModel = obtainViewModel(this@DetailUserActivity)

        setToolbar()

        detailUserViewModel.getDetailUser(username!!)
        detailUserViewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }
        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        detailUserViewModel.isFavoritedUser(username!!)
        detailUserViewModel.isFavorited.observe(this) {
            setFavorite(it)
        }

        binding.fabFavorite.setOnClickListener(this)
        binding.imgPhoto.setOnClickListener(this)

        setTabs()
    }

    private fun setFavorite(isFavorite: Boolean) {
        Log.i("SIUUU", "SIUUU CEKFAVORITE $isFavorite")
        this.isFavorite = isFavorite
        if (isFavorite)
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
        else
            binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border)

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailUserViewModel::class.java)
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
        this.detailUser = detailUser
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
            binding.fabFavorite.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.fabFavorite.visibility = View.VISIBLE

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_favorite -> {

                favoriteUserEntity = FavoriteUserEntity()
                favoriteUserEntity.let { favoriteUserEntity ->
                    favoriteUserEntity?.login = detailUser?.login!!
                    favoriteUserEntity?.avatarUrl = detailUser?.avatarUrl!!
                }

                if (!isFavorite) {
                    detailUserViewModel.insertFavorite(favoriteUserEntity!!)
                    Log.i("SIUUU INSERT", "SIUUUUUUU INSERT")

                } else {
                    detailUserViewModel.deleteFavorite(favoriteUserEntity!!)
                    Log.i("SIUUU DELETE", "SIUUUUUUU DELETE")
                }
//                detailUserViewModel.updateLoading()
//                Handler().postDelayed(Runnable {
//                    detailUserViewModel.isFavoritedUser(username!!)
//                    detailUserViewModel.updateLoading()
//                }, 3000)

                Log.i(
                    "SIUUUU",
                    "SIUUUU $isFavorite ${favoriteUserEntity?.login}${favoriteUserEntity?.avatarUrl} \n ${detailUser?.login} ${detailUser?.avatarUrl}"
                )
            }
            R.id.img_photo->{
                detailUserViewModel.isFavoritedUser(username!!)

            }
        }
    }

}