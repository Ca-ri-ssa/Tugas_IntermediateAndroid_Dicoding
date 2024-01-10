package com.carissa.intermediate.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.carissa.intermediate.R
import com.carissa.intermediate.addstory.UploadStoryActivity
import com.carissa.intermediate.data.preference.UserPreference
import com.carissa.intermediate.databinding.ActivityMainBinding
import com.carissa.intermediate.map.MapsActivity
import com.carissa.intermediate.welcome.WelcomeActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userPreference by lazy {
        UserPreference(this)
    }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setContent()
        uploadStoryButton()
    }

    private fun setView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setContent() {
        showLoading(true)

        userPreference.getUserToken().onEach { token ->
            Log.d("SessionUserToken", "Token: $token")
        }.launchIn(lifecycleScope)

        binding.rvItem.layoutManager = LinearLayoutManager(this)

        getAllDataStory()
    }


    private fun getAllDataStory() {
        val adapter = ListStoryAdapter()

        binding.rvItem.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        lifecycleScope.launch {
            val token = getSessionToken()
            Log.d("getAllDataStory", " Get Token: $token")
            val getToken = "Bearer $token"
            viewModel.getAllStoryPaging(getToken).observe(this@MainActivity) {
                adapter.submitData(lifecycle, it)
                showLoading(false)
                Log.d("getAllDataStory", "Data loaded in: $it")
            }
        }
    }

    private suspend fun getSessionToken(): String {
        return userPreference.getUserToken().first()
    }

    private fun uploadStoryButton() {
        binding.apply {
            fbItemUploadStory.setOnClickListener {
                startActivity(Intent(this@MainActivity, UploadStoryActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllDataStory()
        Log.d("onResume", "data updated")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutButton -> {
                AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Log Out") { _, _ ->
                        lifecycleScope.launch {
                            userPreference.clearUserToken()
                            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
                            finish()
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                return true
            }
            R.id.mapButton -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}