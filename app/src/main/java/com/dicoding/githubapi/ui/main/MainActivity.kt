package com.dicoding.githubapi.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapi.R
import com.dicoding.githubapi.data.remote.response.ItemsItem
import com.dicoding.githubapi.databinding.ActivityMainBinding
import com.dicoding.githubapi.ui.adapter.UserAdapter
import com.dicoding.githubapi.ui.detail.DetailActivity
import com.dicoding.githubapi.ui.favorite.FavoriteActivity
import com.dicoding.githubapi.ui.setting.SettingActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsers.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            val dividerItemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
            setLayoutManager(layoutManager)
            addItemDecoration(dividerItemDecoration)
        }

        mainViewModel.apply {
            items.observe(this@MainActivity) { items ->
                if (items != null) {
                    getUser(items)
                } else {
                    binding.apply {
                        rvUsers.visibility = View.GONE
                        tvNoData.visibility = View.VISIBLE
                    }
                }
            }

            isLoading.observe(this@MainActivity) {
                showLoading(it)
            }

            isError.observe(this@MainActivity) {
                showError(it)
            }
        }
    }

    private fun getUser(items: List<ItemsItem>) {
        val userAdapter = UserAdapter(items)
        binding.rvUsers.adapter = userAdapter

        userAdapter.setOnClickCallback(object : UserAdapter.OnClickCallback {
            override fun onClicked(data: ItemsItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: ItemsItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.apply {
            putExtra(DetailActivity.EXTRA_USER, data.login)
            putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.findUser(query)
                    searchView.clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val favoriteIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
                true
            }
            R.id.theme -> {
                val settingIntent = Intent(this, SettingActivity::class.java)
                startActivity(settingIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showError(state: Boolean) {
        if (state) hideUI(true) else hideUI(false)
    }

    private fun hideUI(state: Boolean) {
        if (state) {
            binding.apply {
                tvError.visibility = View.VISIBLE
                rvUsers.visibility = View.GONE
            }
        } else {
            binding.mainLayout.visibility = View.VISIBLE
        }
    }
}