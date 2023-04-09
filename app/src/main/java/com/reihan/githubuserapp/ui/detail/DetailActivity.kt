package com.reihan.githubuserapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.reihan.githubuserapp.R
import com.reihan.githubuserapp.data.response.database.UserEntity
import com.reihan.githubuserapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = arrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var userEntity: UserEntity


    private val detailViewModel by viewModels<DetailViewModel> {
        DetailViewModel.ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USER)
        Log.d("DetailActivity", "Username: $username")
        val bundle = Bundle()
        bundle.putString(EXTRA_USER, username)

        viewModel = ViewModelProvider(
            this,
            DetailViewModel.ViewModelFactory.getInstance(application)
        )[DetailViewModel::class.java]

        viewModel.getDetailUser(username.toString())
        showLoading(true)
        viewModel.userDetail.observe(this) {
            showLoading(false)
            if (it != null) {
                binding.apply {
                    tvName.text = it.name
                    tvUsername.text = it.login
                    tvFollowers.text = resources.getString(R.string.data_followers, it.followers)
                    tvFollowing.text = resources.getString(R.string.data_following, it.following)
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ciProfile)

                    userEntity = UserEntity(it.id, it.login, it.name, it.avatarUrl)
                }
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.checkFavoriteUser(username.toString()).observe(this) { isFavorite ->
            if (isFavorite) {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite_full
                    )
                )
            } else {
                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite_border
                    )
                )
            }

            binding.fab.setOnClickListener { view ->
                if (view.id == R.id.fab) {
                    if (!isFavorite) {
                        detailViewModel.insertData(userEntity)
                    } else {
                        detailViewModel.deleteData(userEntity)
                    }
                }
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username.toString()
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager)
        { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f

    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}

