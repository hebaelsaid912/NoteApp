package com.example.android.noteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.noteapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
     lateinit var mainBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        replaceFragments(HomeFragment.newInstance(),true)
    }

    private fun replaceFragments(fragment: Fragment, isTransition: Boolean){
        val fragmentTransition = supportFragmentManager.beginTransaction()
        if (isTransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.fragment_container,fragment).addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()

    }
}