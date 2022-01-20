package com.example.whist.ui.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whist.R
import com.example.whist.data.domain.Player
import com.example.whist.databinding.PreviewFragmentBinding
import com.example.whist.ui.viewmodels.preview.PreviewAdapter
import com.example.whist.ui.viewmodels.preview.PreviewViewModel
import com.example.whist.utils.addDecoration

class PreviewFragment : Fragment() {

    private val args by navArgs<PreviewFragmentArgs>()
    private val viewModel: PreviewViewModel by lazy {
        val activity = requireNotNull(this.activity) {

        }
        ViewModelProvider(
            this,
            PreviewViewModel.Factory(activity.application, args)
        )[PreviewViewModel::class.java]
    }
    private var viewModelAdapter: PreviewAdapter? = null
    private lateinit var binding: PreviewFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.players.observe(viewLifecycleOwner, { players ->
            players?.apply {
                viewModelAdapter?.players = players
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.preview_fragment,
            container,
            false
        )
        initBinding()
        onBackPressed()
        navigationNext()
        navigationNBack()
        binding.previewViewModel!!.players.observe(viewLifecycleOwner){
            binding.recyclerViewPreview.invalidate()
        }
//        navigationWithButton(binding.buttonPrevBack,PreviewFragmentDirections.actionPreviewFragmentToRoomFragment())
//        navigationWithButton(binding.buttonPrevNext,PreviewFragmentDirections.actionPreviewFragmentToLoginTableFragment())
        return binding.root
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.previewViewModel = viewModel
        viewModelAdapter = PreviewAdapter()
        binding.root.findViewById<RecyclerView>(R.id.recycler_view_preview).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
        addDecoration(binding.recyclerViewPreview, context)
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController()
                    .navigate(PreviewFragmentDirections.actionPreviewFragmentToRoomFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun navigationNext() {
        binding.buttonPrevNext.setOnClickListener {
            findNavController()
                .navigate(PreviewFragmentDirections.actionPreviewFragmentToLoginTableFragment(args.table))
        }
    }

    private fun navigationNBack() {
        binding.buttonPrevBack.setOnClickListener {
            findNavController()
                .navigate(PreviewFragmentDirections.actionPreviewFragmentToRoomFragment())
        }

    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding.unbind()
//        binding.previewViewModel?.players?.removeObservers(viewLifecycleOwner)
//        viewModelAdapter = null
//    }

}