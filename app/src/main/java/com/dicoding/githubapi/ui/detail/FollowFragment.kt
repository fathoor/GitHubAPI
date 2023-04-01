package com.dicoding.githubapi.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapi.databinding.FragmentFollowBinding
import com.dicoding.githubapi.model.ItemsItem
import com.dicoding.githubapi.ui.adapter.UserAdapter

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val followViewModel by viewModels<FollowViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        position = arguments?.getInt(ARG_POSITION, 0)
        username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(binding.rvFollow.context, layoutManager.orientation)
        binding.rvFollow.addItemDecoration(dividerItemDecoration)

        getUserFollow(username, position)

        followViewModel.listFollower.observe(viewLifecycleOwner) { listFollower ->
            if (listFollower != null) {
                showListFollower(listFollower)
            }
        }

        followViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
            if (listFollowing != null) {
                showListFollowing(listFollowing)
            }
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.isError.observe(viewLifecycleOwner) {
            showError(it)
        }
    }


    private fun getUserFollow(username: String?, position: Int?) {
        if (position == 1) {
            followViewModel.getUserFollower(username.toString())
        } else {
            followViewModel.getUserFollowing(username.toString())
        }
    }

    private fun showListFollower(listFollower: List<ItemsItem>) {
        if (listFollower.isNotEmpty()) {
            val followerAdapter = UserAdapter(listFollower)
            binding.rvFollow.adapter = followerAdapter

            followerAdapter.setOnClickCallback(object : UserAdapter.OnClickCallback {
                override fun onClicked(data: ItemsItem) {
                    showSelectedUser(data)
                }
            })

            binding.rvFollow.visibility = View.VISIBLE
            binding.tvError.visibility = View.GONE
        } else {
            binding.rvFollow.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
        }
    }

    private fun showListFollowing(listFollowing: List<ItemsItem>) {
        if (listFollowing.isNotEmpty()) {
            val followingAdapter = UserAdapter(listFollowing)
            binding.rvFollow.adapter = followingAdapter

            followingAdapter.setOnClickCallback(object : UserAdapter.OnClickCallback {
                override fun onClicked(data: ItemsItem) {
                    showSelectedUser(data)
                }
            })

            binding.rvFollow.visibility = View.VISIBLE
            binding.tvError.visibility = View.GONE
        } else {
            binding.rvFollow.visibility = View.GONE
            binding.tvError.visibility = View.VISIBLE
        }
    }

    private fun showSelectedUser(data: ItemsItem) {
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, data.login)
        startActivity(intent)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showError(state: Boolean) {
        binding.tvError.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
        var position: Int? = null
        var username: String? = null
    }
}