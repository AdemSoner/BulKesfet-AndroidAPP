package com.example.bulkesfet.view.splashAndOnBoard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bulkesfet.view.splashAndOnBoard.screens.FirstScreen
import com.example.bulkesfet.view.splashAndOnBoard.screens.SecondScreen
import com.example.bulkesfet.databinding.FragmentOnBoardingBinding
import com.example.bulkesfet.adapter.OnBoardAdapter


class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get()= _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(), SecondScreen()
        )
        val adapter = OnBoardAdapter(
            fragmentList, requireActivity().supportFragmentManager, lifecycle
        )
        binding.onBoardingViewPager.adapter = adapter

        return binding.root
    }


}