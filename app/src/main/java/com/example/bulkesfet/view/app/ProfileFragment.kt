package com.example.bulkesfet.view.app

import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.bulkesfet.databinding.FragmentProfileBinding
import com.example.bulkesfet.service.MainListener
import com.example.bulkesfet.viewModel.ProfileViewModel


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    lateinit var activityListener: MainListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(inflater,container,false)
        activityListener = activity as MainListener
        viewModel=ViewModelProviders.of(this)[ProfileViewModel::class.java]
        viewModel.userLogDetail()
        initializeUI()
        observeLiveData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyboardListener(view.rootView)
    }



    private fun initializeUI() {
        binding.loginButton.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.registerButton.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToRegisterFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun observeLiveData() {
        viewModel.loading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                if (it)
                    print("Loading Çalışıyor")
                else
                    print("Loading Çalışmayı durdurdu")
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error->
            error?.let {
                print("Hata = $error")
            }
        })
        viewModel.userLoggedIn.observe(viewLifecycleOwner, Observer { value->
            value?.let {
                setUserDatas(it)
            }
        })
    }
    private fun setUserDatas(value: Boolean) {
        if (value){
            binding.userLoggedInConstraint.visibility=View.VISIBLE
            binding.userLoggedOutConstraint.visibility=View.GONE
        }else{
            binding.userLoggedInConstraint.visibility=View.GONE
            binding.userLoggedOutConstraint.visibility=View.VISIBLE
        }
    }
    private fun keyboardListener(rootView: View?) {
        rootView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.rootView?.height

            val keypadHeight = screenHeight?.minus(r.bottom)

            if (keypadHeight != null) {
                if (keypadHeight > screenHeight * 0.15)
                    activityListener.showOrHide(false)
                else
                    activityListener.showOrHide(true)
            }
        }
    }
}



// TODO BURAYA ESKİ YORUMLARI GÖRÜNTÜLEME KISMI EKLE