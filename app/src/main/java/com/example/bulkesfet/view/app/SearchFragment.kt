package com.example.bulkesfet.view.app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bulkesfet.databinding.FragmentSearchBinding
import com.example.bulkesfet.adapter.PlaceAdapter
import com.example.bulkesfet.viewModel.SearchViewModel


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private val placeAdapter = PlaceAdapter(arrayListOf())
    var myLayoutManager: LinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this)[SearchViewModel::class.java]
        myLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerviewPlace.layoutManager = myLayoutManager
        binding.recyclerviewPlace.adapter = placeAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var query="null"
        arguments?.let {
            if (SearchFragmentArgs.fromBundle(it).category!="category")
                query=SearchFragmentArgs.fromBundle(it).category
        }
        viewModel.refreshDataFromFirebase(query)
        observeLiveData()

        binding.searchView.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(v.text.toString())
                true
            } else {
                false
            }
        }


    }
    private fun performSearch(query: String) {
        viewModel.getDataFromCity(query)
    }
    private fun observeLiveData() {
        viewModel.placeLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it) {
                    binding.recyclerviewPlace.visibility = View.GONE
                    binding.searchProgressBar.visibility = View.VISIBLE
                } else {
                    binding.recyclerviewPlace.visibility = View.VISIBLE
                    binding.searchProgressBar.visibility = View.GONE
                }
            }
        })

        viewModel.placeList.observe(viewLifecycleOwner, Observer { places ->
            places?.let {
                if (it.isEmpty())
                    viewModel.placeError.value=true
                else
                    placeAdapter.updatePlaceList(places)
            }
        })

        viewModel.placeError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    binding.searchFailedTextView.visibility = View.VISIBLE
                    binding.recyclerviewPlace.visibility = View.GONE
                } else {
                    binding.recyclerviewPlace.visibility = View.VISIBLE
                    binding.searchFailedTextView.visibility = View.GONE
                }
            }
        })
    }


}