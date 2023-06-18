package com.example.bulkesfet.view.app

import android.content.Context
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
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.FragmentProfileBinding
import com.example.bulkesfet.service.MainListener
import com.example.bulkesfet.utils.getImage
import com.example.bulkesfet.utils.progressDrawable
import com.example.bulkesfet.viewModel.ProfileViewModel


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel
    lateinit var activityListener: MainListener
    lateinit var userUID:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentProfileBinding.inflate(inflater,container,false)
        activityListener = activity as MainListener
        viewModel=ViewModelProviders.of(this)[ProfileViewModel::class.java]
        viewModel.userLogDetail()

        observeLiveData(binding.root.context)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyboardListener(view.rootView)
        initializeUI(view)
    }


    private fun initializeUI(view: View) {
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
        binding.rateTextView.setOnClickListener {
            val action=ProfileFragmentDirections.actionProfileFragmentToUserCommentsFragment(userUID)
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun observeLiveData(context:Context) {
        viewModel.loading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                if (it){
                    binding.userLoggedInConstraint.visibility=View.GONE
                    binding.userLoggedOutConstraint.visibility=View.GONE
                    binding.profileProgressBar.visibility=View.VISIBLE
                }
                else{
                    binding.profileProgressBar.visibility=View.GONE
            }
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
        viewModel.user.observe(viewLifecycleOwner, Observer { user->
            user?.let {
                binding.profileImage.getImage(it.imageURL, progressDrawable(context))
                binding.userEmail.text=it.email
                binding.userNameSurnameText.text=it.nameSurname
            }
        })
        viewModel.comment.observe(viewLifecycleOwner, Observer { comment->
            comment?.let {
                binding.placeNameText.text=it.placeName
                binding.placeRateText.text=it.rate.toString()
                binding.placeImg.getImage(it.placeImage, progressDrawable(context))
                setStarts(it.rate)
                val userDate=it.date
                val date=userDate.day+"/"+userDate.month+"/"+userDate.year
                binding.commentDateText.text=date
                binding.userComment.text=it.comment
            }
        })
        viewModel.loadUserConstraint.observe(viewLifecycleOwner, Observer { load->
            load?.let {
                if (it) {
                    viewModel.loading.value = false
                    binding.userLoggedInConstraint.visibility = View.VISIBLE
                }
            }
        })
        viewModel.commentCount.observe(viewLifecycleOwner, Observer { value->
            value?.let {
                val string="($it)"
                binding.commentCountTextView.text=string
                if (it==0){
                    binding.userLastCommentGroup.visibility=View.GONE
                    binding.noCommentText.visibility=View.VISIBLE
                }
                else{
                    binding.noCommentText.visibility=View.GONE
                    binding.userLastCommentGroup.visibility=View.VISIBLE
                }


            }
        })
    }

    private fun setStarts(rate: Int) {
        val starList= arrayListOf(binding.starOne,binding.starTwo,binding.starThree,binding.starFour,binding.starFive)
        for (a in rate until 5){
            starList[a].setImageResource(R.drawable.ic_starborder)
        }
        for (a in rate downTo  1){
            starList[a-1].setImageResource(R.drawable.ic_starfull)
        }

    }

    private fun setUserDatas(value: Boolean) {
        if (value){
            userUID=viewModel.getUserUID()
            viewModel.getViewComponents()
        }

        else{
            binding.userLoggedOutConstraint.visibility=View.VISIBLE
            viewModel.loading.value=false
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

