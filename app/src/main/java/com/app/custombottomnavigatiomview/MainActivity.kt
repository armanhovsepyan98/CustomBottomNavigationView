package com.app.custombottomnavigatiomview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.app.custombottomnavigatiomview.databinding.ActivityMainBinding
import com.app.custombottomnavigatiomview.fragments.Favorite
import com.app.custombottomnavigatiomview.fragments.Place
import com.app.custombottomnavigatiomview.fragments.Home
import com.app.custombottomnavigatiomview.fragments.Profile
import com.app.custombottomnavigatiomview.interfaces.OnBottomNavItemClickedListener
import com.app.custombottomnavigatiomview.interfaces.OnFabClickListener
import com.app.custombottomnavigatiomview.model.BottomNavigationData
import com.app.custombottomnavigatiomview.util.delayed
import com.app.custombottomnavigatiomview.util.onClick
import com.app.custombottomnavigatiomview.util.showFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showFragment<Home>()
        setOnFabClickListener()
        initBottomNavigation()
    }

    private fun setOnFabClickListener() {
        binding.floatingBtn.setFabClickListener(object :
            OnFabClickListener.OnSmallFabClickListener {
            override fun onSmallFabClick(position: Int) {
                when (position) {
                    1 -> {
                        Toast.makeText(applicationContext,getString(R.string.text),Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(applicationContext,getString(R.string.camera),Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(applicationContext,getString(R.string.mic),Toast.LENGTH_SHORT).show()
                    }
                }
                delayed(500) {
                    binding.transparentView.performClick()
                }
            }
        })

        binding.floatingBtn.setBigFabClickListener(object :
            OnFabClickListener.OnBigFabClickListener {
            override fun onBigFabClick(state: Boolean) {
                val animation = binding.transparentView.animate()
                    .alpha(if (state) 0f else 1f)
                    .setDuration(300)

                if (!state) {
                    binding.transparentView.isVisible = true
                    binding.transparentView.alpha = 0f
                } else {
                    animation.withEndAction {
                        binding.transparentView.isVisible = false
                    }
                }
                animation.start()
            }
        })


        binding.transparentView.onClick {
            binding.transparentView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    binding.transparentView.isVisible = false
                }
                .start()

            binding.floatingBtn.disableFab(true)
        }
    }

    private fun initBottomNavigation() {
        val navigationData = arrayOf(
            BottomNavigationData(BottomNavigationType.HOME.id, R.drawable.ic_home, getString(R.string.home)),
            BottomNavigationData(BottomNavigationType.PLACE.id, R.drawable.ic_place,  getString(R.string.place)),
            BottomNavigationData(BottomNavigationType.FAVORITE.id, R.drawable.ic_favorite,  getString(R.string.favorite)),
            BottomNavigationData(BottomNavigationType.PROFILE.id, R.drawable.ic_profile,  getString(R.string.profile))
        )
        binding.bottomNavBar.bindData(navigationData)
        binding.bottomNavBar.setOnBottomNavItemClickedListener(object :
            OnBottomNavItemClickedListener {
            override fun onItemClicked(index: Int) {
                when (index) {
                    0 -> {
                        binding.bottomNavBar.selectItemAtIndex(index)
                        showFragment<Home>()
                    }
                    1 -> {
                        binding.bottomNavBar.selectItemAtIndex(index)
                        showFragment<Place>()
                    }
                    3 -> {
                        binding.bottomNavBar.selectItemAtIndex(index)
                        showFragment<Favorite>()
                    }
                    else -> {
                        binding.bottomNavBar.selectItemAtIndex(index)
                        showFragment<Profile>()
                    }
                }
            }
        })
        binding.bottomNavBar.selectItemAtIndex(BottomNavigationType.HOME.ordinal)
    }
}

enum class BottomNavigationType(val id: Int) {
    HOME(0),
    PLACE(1),
    FAVORITE(3),
    PROFILE(4);
}
