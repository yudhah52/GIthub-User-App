package com.yhezra.githubapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.databinding.ItemUserBinding
import com.yhezra.githubapp.data.remote.model.UserItem

class ListUserAdapter(private val list: List<Any>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val USER_ITEM_VIEW_TYPE = 0
        private const val FAVORITE_USER_ITEM_VIEW_TYPE = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is UserItem) USER_ITEM_VIEW_TYPE else FAVORITE_USER_ITEM_VIEW_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return if (viewType == USER_ITEM_VIEW_TYPE) {
            UserViewHolder(binding)
        } else {
            FavoriteUserViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserViewHolder -> holder.bind(list[position] as UserItem)
            is FavoriteUserViewHolder -> holder.bind(list[position] as FavoriteUserEntity)
        }
    }

    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserItem) {
            val avatarUrl = user.avatarUrl
            val username = user.login
            binding.tvItemName.text = username
            Glide.with(binding.root)
                .load(avatarUrl)
                .into(binding.imgItemPhoto)
            binding.root.setOnClickListener {
                onItemClickCallback.onItemUserClicked(user)
            }
        }
    }

    inner class FavoriteUserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteUser: FavoriteUserEntity) {
            val avatarUrl = favoriteUser.avatarUrl
            val username = favoriteUser.login
            binding.tvItemName.text = username
            Glide.with(binding.root)
                .load(avatarUrl)
                .into(binding.imgItemPhoto)
            binding.root.setOnClickListener {
                onItemClickCallback.onItemFavoriteClicked(favoriteUser)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemFavoriteClicked(item: FavoriteUserEntity)
        fun onItemUserClicked(user:UserItem)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}