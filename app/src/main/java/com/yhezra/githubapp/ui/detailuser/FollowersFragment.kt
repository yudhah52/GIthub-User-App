package com.yhezra.githubapp.ui.detailuser

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yhezra.githubapp.data.local.entity.FavoriteUserEntity
import com.yhezra.githubapp.databinding.FragmentFollowBinding
import com.yhezra.githubapp.data.remote.model.UserItem
import com.yhezra.githubapp.helper.ViewModelFactory
import com.yhezra.githubapp.ui.home.adapter.ListUserAdapter

class FollowersFragment : Fragment() {

    private var firstVisibleItemPosition = 0
    private var lastTotalItem = 0
    private var totalItemCount = 0
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private var username = ""

    private lateinit var detailUserViewModel: DetailUserViewModel

    companion object {
        private const val EXTRA_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            username = arguments?.getString(EXTRA_USERNAME).toString()
        }
        detailUserViewModel = obtainViewModel(requireActivity() as AppCompatActivity)

        detailUserViewModel.getFollowers(username)
        detailUserViewModel.listFollowers.observe(viewLifecycleOwner) { listFollowers ->
            setListFollowers(listFollowers)
        }
        detailUserViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailUserViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setListFollowers(listFollowers: List<UserItem>) {

        val adapter = ListUserAdapter(listFollowers)

        adapter.setOnItemClickCallback(object :
            ListUserAdapter.OnItemClickCallback {
            override fun onItemFavoriteClicked(item: FavoriteUserEntity) {

            }

            override fun onItemUserClicked(user: UserItem) {
                val moveToDetailUser = Intent(requireContext(), DetailUserActivity::class.java)
                moveToDetailUser.putExtra(DetailUserActivity.USERNAME, user.login)
                startActivity(moveToDetailUser)
            }
        })

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)

        binding.rvUserFollow.layoutManager = layoutManager
        binding.rvUserFollow.addItemDecoration(itemDecoration)
        binding.rvUserFollow.adapter = adapter
        binding.rvUserFollow.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                totalItemCount = layoutManager.itemCount
                val isLoading = detailUserViewModel.isLoading.value ?: false
                if ((lastVisibleItemPosition == totalItemCount - 1) && (lastTotalItem != totalItemCount) && !isLoading) {
                    detailUserViewModel.updatePage(isFollowers = true)
                    detailUserViewModel.getFollowers(username)
                    lastTotalItem = totalItemCount
                }
            }
        })
        if (lastTotalItem == totalItemCount) {
            layoutManager.scrollToPosition(firstVisibleItemPosition + 1)
        }
    }

}