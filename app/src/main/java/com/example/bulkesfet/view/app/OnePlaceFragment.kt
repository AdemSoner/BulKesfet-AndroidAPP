package com.example.bulkesfet.view.app

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.bulkesfet.R
import com.example.bulkesfet.databinding.FragmentOnePlaceBinding
import com.example.bulkesfet.utils.getImage
import com.example.bulkesfet.utils.progressDrawable
import com.example.bulkesfet.viewModel.OnePlaceViewModel


class OnePlaceFragment : Fragment() {
    private var _binding: FragmentOnePlaceBinding? = null
    private val binding get()=_binding!!
    private lateinit var viewModel: OnePlaceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentOnePlaceBinding.inflate(inflater,container,false)
        viewModel=ViewModelProviders.of(this)[OnePlaceViewModel::class.java]
        arguments?.let {
            val mID= OnePlaceFragmentArgs.fromBundle(it).mId
            viewModel.getDatas(mID)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData(view)
        initializeUI(view)
    }

    private fun initializeUI(view: View) {
        val favBtn=binding.bookmarkCheck

        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        if (viewModel.getUser()){
            //TODO Kullanıcı girişi var ise burada da favori kontrolü yap
            favBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked)
                    favBtn.setButtonDrawable(R.drawable.ic_removebookmark)
                else
                    favBtn.setButtonDrawable(R.drawable.ic_addbookmark)
            }
        }else{
            favBtn.setButtonDrawable(R.drawable.bookmark)
            favBtn.setOnClickListener {
                val alert= AlertDialog.Builder(view.context)
                alert.setTitle(R.string.needLogin)
                    .setIcon(R.drawable.ic_person_add)
                    .setNeutralButton(R.string.ok) { dialog, _ ->
                        dialog.cancel()
                    }
                    .create().show()
            }
            }
        }


    private fun observeLiveData(view: View) {
        viewModel.loading.observe(viewLifecycleOwner, Observer {loading ->
            loading?.let {
                    enableDisableComponents(it)
            }



        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Toast.makeText(context,it,Toast.LENGTH_LONG).show()
            }

        })
        viewModel.placeDetails.observe(viewLifecycleOwner, Observer { details ->
            details?.let {
                Log.e("UYGULAMA","Detaylar burada"+ it.placeName)
                binding.placeNameText.text=it.placeName
                binding.placeDescription.text=it.description
                binding.placePrice.text=it.price
                binding.placeAddress.text=it.address
                binding.viewPager.getImage(it.images[0], progressDrawable(requireContext()))

            }
        })
    }

    private fun enableDisableComponents(value: Boolean) {
        if (value)
            binding.mainConstraint.visibility=View.GONE
        else
            binding.mainConstraint.visibility=View.VISIBLE

    }


}