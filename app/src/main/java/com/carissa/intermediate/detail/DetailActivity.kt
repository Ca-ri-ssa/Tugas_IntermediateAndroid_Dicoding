package com.carissa.intermediate.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.carissa.intermediate.R
import com.carissa.intermediate.data.preference.UserPreference
import com.carissa.intermediate.databinding.ActivityDetailBinding
import com.carissa.intermediate.util.TimePost
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val userPreference by lazy {
        UserPreference(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setContent()
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
        supportActionBar?.title = getString(R.string.app_name_detail)
    }

    private fun setContent() {
        showLoading(true)

        lifecycleScope.launch {
            getSessionToken()
        }

        viewModel.getStoryDetailDataLive().observe(this) { storyDetail ->
            if (storyDetail != null) {
                showLoading(false)
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load(storyDetail.photoUrl)
                        .centerCrop()
                        .into(ivDetailPhoto)
                    tvDetailName.text = storyDetail.name
                    tvDetailDescription.text = storyDetail.description
                    val formattedDate = TimePost.formatDatabaseDate(storyDetail.createdAt, "dd MMM yyyy HH:mm:ss")
                    tvTimePost.text = formattedDate
                }
            }
        }
    }

    private suspend fun getSessionToken() {
        val token = userPreference.getUserToken().first()
        val id = intent.getStringExtra("detail_story")
        if (id != null) {
            viewModel.getStoryDetail(token, id)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}