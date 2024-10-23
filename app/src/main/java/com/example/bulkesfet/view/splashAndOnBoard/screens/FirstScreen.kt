package com.example.bulkesfet.view.splashAndOnBoard.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private var _binding: FragmentFirstScreenBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        val viewPagerFrag = activity?.findViewById<ViewPager2>(R.id.onBoardingViewPager)

        binding.btnNext.setOnClickListener {
            viewPagerFrag?.currentItem = 1
        }
        return binding.root
    }


}