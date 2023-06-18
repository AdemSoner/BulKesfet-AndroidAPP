package com.example.bulkesfet.view.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bulkesfet.R
import com.example.bulkesfet.adapter.PlaceCommentAdapter
import com.example.bulkesfet.databinding.FragmentPlaceCommentsBinding
import com.example.bulkesfet.model.NewCommentModel

import com.example.bulkesfet.viewModel.PlaceCommentViewModel

class PlaceCommentsFragment : Fragment() {
    private var _binding: FragmentPlaceCommentsBinding?=null
    private val binding get()=_binding!!
    lateinit var viewModel:PlaceCommentViewModel
    lateinit var placeID:String
    lateinit var placeName:String
    lateinit var placeImage:String
    private val commentAdapter=PlaceCommentAdapter(arrayListOf())
    var myLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentPlaceCommentsBinding.inflate(inflater,container,false)
        viewModel=ViewModelProviders.of(this)[PlaceCommentViewModel::class.java]
        arguments?.let {
            placeID=PlaceCommentsFragmentArgs.fromBundle(it).placeID
            placeName=PlaceCommentsFragmentArgs.fromBundle(it).placeName
            placeImage=PlaceCommentsFragmentArgs.fromBundle(it).placeImage
        }
        myLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.commentsRecyclerView.layoutManager = myLayoutManager
        binding.commentsRecyclerView.adapter=commentAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()
        initializeUI(view)
        if (viewModel.getUser())
            viewModel.getUserDetails()
        else
            binding.newCommentTextView.visibility=View.GONE

        viewModel.getCommentsFromFirebase(placeID)
    }

    private fun initializeUI(view: View) {
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.newCommentTextView.setOnClickListener {
            viewModel.userDetail.value=NewCommentModel(placeID,placeName,placeImage,viewModel.userName.value!!,viewModel.userImage.value!!)
            NewCommentFragment().show(childFragmentManager,"newCommentFragment")
        }
        binding.emptyComment.setOnClickListener {
            viewModel.userDetail.value=NewCommentModel(placeID,placeName,placeImage,viewModel.userName.value!!,viewModel.userImage.value!!)
            NewCommentFragment().show(childFragmentManager,"newCommentFragment")
        }
    }


    private fun observeLiveData() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { loading->
            loading?.let {
                //TODO Yüklenirken olacakları ayarla
            }
        })
        viewModel.commentCount.observe(viewLifecycleOwner, Observer { count->
            count?.let {
                if(it>0){
                    var string=getString(R.string.commentCount)
                    string= String.format(string, it)
                    binding.commentCountTextView.text=string
                    binding.emptyComment.visibility=View.GONE
                    binding.commentsRecyclerView.visibility=View.VISIBLE
                }else{
                    binding.commentCountTextView.text=getString(R.string.noCommentFound)
                    binding.emptyComment.visibility=View.VISIBLE
                    binding.commentsRecyclerView.visibility=View.GONE
                }
            }
        })
        viewModel.commentList.observe(viewLifecycleOwner, Observer { list->
            list?.let {
                if (it.isEmpty())
                    viewModel.commentError.value=true
                else
                    commentAdapter.updatePlaceList(it)
            }
        })
        viewModel.commentChecker.observe(viewLifecycleOwner, Observer {chkec->
            chkec?.let {
                if (it==1){
                    binding.newCommentTextView.visibility=View.GONE
                }
            }
        })

    }




}