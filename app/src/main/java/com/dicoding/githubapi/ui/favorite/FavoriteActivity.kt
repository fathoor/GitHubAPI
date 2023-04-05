package com.dicoding.githubapi.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapi.R
import com.dicoding.githubapi.data.local.entity.FavoriteEntity
import com.dicoding.githubapi.databinding.ActivityFavoriteBinding
import com.dicoding.githubapi.ui.adapter.FavoriteAdapter
import com.dicoding.githubapi.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        FavoriteViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.favorite)

        binding.rvFavorite.apply {
            val layoutManager = LinearLayoutManager(this@FavoriteActivity)
            val dividerItemDecoration = DividerItemDecoration(this@FavoriteActivity, layoutManager.orientation)
            setLayoutManager(layoutManager)
            addItemDecoration(dividerItemDecoration)
        }

        favoriteViewModel.showFavorite().observe(this) { favorite ->
            if (favorite.isNotEmpty()) {
                showFavoriteList(favorite)
            } else {
                binding.rvFavorite.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun showFavoriteList(favorite: List<FavoriteEntity>) {
        val favoriteAdapter = FavoriteAdapter(favorite)
        binding.rvFavorite.adapter = favoriteAdapter

        favoriteAdapter.setOnClickCallback(object : FavoriteAdapter.OnClickCallback {
            override fun onClicked(data: FavoriteEntity) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: FavoriteEntity) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_USER, data.username)
        intent.putExtra(DetailActivity.EXTRA_AVATAR, data.avatar)
        startActivity(intent)
    }
}