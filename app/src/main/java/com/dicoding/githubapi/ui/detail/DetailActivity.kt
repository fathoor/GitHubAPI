package com.dicoding.githubapi.ui.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.githubapi.R
import com.dicoding.githubapi.databinding.ActivityDetailBinding
import com.dicoding.githubapi.model.DetailUserResponse
import com.dicoding.githubapi.ui.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUser()

        detailViewModel.detail.observe(this) { detail ->
            setDetailData(detail)
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.isError.observe(this) {
            showError(it)
        }
    }

    private fun getUser() {
        val username = intent.getStringExtra(EXTRA_USER)
        if (username != null) {
            detailViewModel.setDetailUser(username)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username

        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun setDetailData(detail: DetailUserResponse) {
        Glide.with(this)
            .load(detail.avatarUrl)
            .into(binding.ivAvatar)
        binding.tvFullName.text = detail.name
        binding.tvUsername.text = this.resources.getString(R.string.username, detail.login)
        binding.tvFollowers.text = detail.followers.toString()
        binding.tvFollowing.text = detail.following.toString()
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showError(state: Boolean) {
        if (state) {
            hideUI(true)
        } else {
            hideUI(false)
        }
    }

    private fun hideUI(state: Boolean) {
        if (state) {
            binding.tvError.visibility = View.VISIBLE
            binding.ivAvatar.visibility = View.GONE
            binding.tvFullName.visibility = View.GONE
            binding.tvUsername.visibility = View.GONE
            binding.tvFollowersWrapper.visibility = View.GONE
            binding.tvFollowingWrapper.visibility = View.GONE
            binding.tvFollowers.visibility = View.GONE
            binding.tvFollowing.visibility = View.GONE
            binding.tabs.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
        } else {
            binding.detailLayout.visibility = View.VISIBLE
        }
    }
}