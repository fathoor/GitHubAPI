package com.dicoding.githubapi.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubapi.R
import com.dicoding.githubapi.data.local.preference.SettingPreferences
import com.dicoding.githubapi.data.local.preference.dataStore
import com.dicoding.githubapi.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.theme)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.apply {
            getThemeSetting().observe(this@SettingActivity) {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.switchTheme.isChecked = false
                }
            }

            binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
                saveThemeSetting(isChecked)
                if (isChecked) {
                    Toast.makeText(this@SettingActivity, R.string.dark, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SettingActivity, R.string.light, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}