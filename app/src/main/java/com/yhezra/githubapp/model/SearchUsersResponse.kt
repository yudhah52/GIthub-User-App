package com.yhezra.githubapp.model

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: List<UserItem>
)
