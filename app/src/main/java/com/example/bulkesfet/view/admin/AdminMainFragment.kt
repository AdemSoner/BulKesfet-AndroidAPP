package com.example.bulkesfet.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.FragmentAdminMainBinding
import com.example.bulkesfet.viewModel.AdminMainViewModel

class AdminMainFragment : Fragment() {
    private var _binding: FragmentAdminMainBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: AdminMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminMainBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[AdminMainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        observeLiveData()
    }

    private fun initializeUI() {
        binding.logoutAdminBTN.setOnClickListener {
            viewModel.logout()
        }
        binding.placeRequestsFragmentBTN.setOnClickListener {
            val action=AdminMainFragmentDirections.actionAdminMainFragmentToPlaceRequestsFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.commentsFragmentBTN.setOnClickListener {
            val action=AdminMainFragmentDirections.actionAdminMainFragmentToAllCommentsFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

    }

    private fun observeLiveData() {
        viewModel.logout.observe(viewLifecycleOwner, Observer { value ->
            value?.let {
                if (it) {
                    val action =
                        AdminMainFragmentDirections.actionAdminMainFragmentToLoginFragment()
                    Navigation.findNavController(binding.root).navigate(action)
                }
            }
        })
    }


}