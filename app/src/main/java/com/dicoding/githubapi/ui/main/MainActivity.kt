package com.dicoding.githubapi.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubapi.R
import com.dicoding.githubapi.databinding.ActivityMainBinding
import com.dicoding.githubapi.model.ItemsItem
import com.dicoding.githubapi.ui.adapter.UserAdapter
import com.dicoding.githubapi.ui.detail.DetailActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(dividerItemDecoration)

        mainViewModel.items.observe(this) { items ->
            if (items != null) {
                getUser(items)
            } else {
                binding.rvUsers.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.isError.observe(this) {
            showError(it)
        }
    }

    private fun getUser(items: List<ItemsItem>) {
        if (items.isNotEmpty()) {
            showUser(items)

            binding.rvUsers.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
        } else {
            binding.rvUsers.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
        }
    }

    private fun showUser(items: List<ItemsItem>) {
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
        intent.putExtra(DetailActivity.EXTRA_USER, data.login)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showError(state: Boolean) {
        if (state) hideUI(true) else hideUI(false)
    }

    private fun hideUI(state: Boolean) {
        if (state) {
            binding.tvError.visibility = View.VISIBLE
            binding.rvUsers.visibility = View.GONE
        } else {
            binding.mainLayout.visibility = View.VISIBLE
        }
    }
}