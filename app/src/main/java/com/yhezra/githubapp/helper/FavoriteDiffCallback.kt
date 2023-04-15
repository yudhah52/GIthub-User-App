package com.yhezra.githubapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity

class FavoriteDiffCallback(private val oldFavoriteList: List<FavoriteUserEntity>, private val newFavoriteList: List<FavoriteUserEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteList.size
    override fun getNewListSize(): Int = newFavoriteList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].login == newFavoriteList[newItemPosition].login
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFavorite = oldFavoriteList[oldItemPosition]
        val newFavorite = newFavoriteList[newItemPosition]
        return oldFavorite.avatarUrl == newFavorite.avatarUrl && oldFavorite.login == newFavorite.login
    }
}