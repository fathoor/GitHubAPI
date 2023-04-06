package com.dicoding.githubapi.ui.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.githubapi.R
import com.dicoding.githubapi.data.local.entity.FavoriteEntity
import com.dicoding.githubapi.databinding.ActivityDetailBinding
import com.dicoding.githubapi.data.remote.response.DetailUserResponse
import com.dicoding.githubapi.ui.adapter.SectionsPagerAdapter
import com.dicoding.githubapi.ui.favorite.FavoriteViewModel
import com.dicoding.githubapi.ui.favorite.FavoriteViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(application)
    }
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUser()

        detailViewModel.apply {
            detail.observe(this@DetailActivity) { detail ->
                setDetailData(detail)
            }

            isLoading.observe(this@DetailActivity) {
                showLoading(it)
            }

            isError.observe(this@DetailActivity) {
                showError(it)
            }
        }

        favoriteViewModel.apply {
            val username = intent.getStringExtra(EXTRA_USER).toString()
            val avatar = intent.getStringExtra(EXTRA_AVATAR).toString()

            getUser(username).observe(this@DetailActivity) { favorite ->
                if (favorite != null) {
                    isFavorite = true
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite_fill)
                } else {
                    isFavorite = false
                    binding.fabFavorite.setImageResource(R.drawable.ic_favorite)
                }
            }

            val favoriteUser = FavoriteEntity(username, avatar)

            binding.fabFavorite.setOnClickListener {
                isFavorite = if (isFavorite) {
                    favoriteViewModel.deleteUser(favoriteUser)
                    Toast.makeText(this@DetailActivity, getString(R.string.removed, username), Toast.LENGTH_SHORT).show()
                    false
                } else {
                    favoriteViewModel.insertUser(favoriteUser)
                    Toast.makeText(this@DetailActivity, getString(R.string.added, username), Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }

    private fun getUser() {
        val username = intent.getStringExtra(EXTRA_USER).toString()
        detailViewModel.setDetailUser(username)

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
        binding.apply {
            tvFullName.text = detail.name
            tvUsername.text = this@DetailActivity.resources.getString(R.string.username, detail.login)
            tvFollowers.text = detail.followers.toString()
            tvFollowing.text = detail.following.toString()
        }
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
            binding.apply {
                tvError.visibility = View.VISIBLE
                ivAvatar.visibility = View.GONE
                tvFullName.visibility = View.GONE
                tvUsername.visibility = View.GONE
                tvFollowersWrapper.visibility = View.GONE
                tvFollowingWrapper.visibility = View.GONE
                tvFollowers.visibility = View.GONE
                tvFollowing.visibility = View.GONE
                tabs.visibility = View.GONE
                viewPager.visibility = View.GONE
            }
        } else {
            binding.detailLayout.visibility = View.VISIBLE
        }
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.follower,
            R.string.following
        )
    }
}