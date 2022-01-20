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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whist.R
import com.example.whist.data.domain.Player
import com.example.whist.databinding.TableFragmentBinding
import com.example.whist.services.fcm.unSubscribeTopic
import com.example.whist.ui.viewmodels.preview.PreviewAdapter
import com.example.whist.ui.viewmodels.table.TableViewModel
import com.example.whist.utils.*

private const val TOPIC = TABLE_TOPIC
private const val SEND_TABLE = TABLE
private const val SEND_PLAYER = PLAYER
private const val SEND_ROOM = ROOM

class TableFragment : Fragment() {

    private val args by navArgs<TableFragmentArgs>()
    private val viewModel: TableViewModel by lazy {
        val activity = requireNotNull(this.activity) {

        }
        ViewModelProvider(
            this,
            TableViewModel.Factory(activity.application, args)
        )[TableViewModel::class.java]
    }
    private var viewModelAdapter: PreviewAdapter? = null
    private lateinit var binding: TableFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.players.observe(viewLifecycleOwner, Observer<List<Player>> { players ->
            players?.apply {
                binding.recyclerViewTable.invalidate()
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
            R.layout.table_fragment,
            container,
            false
        )
        initBinding()
        onBackPressed()
        navigationNext()
        navigationNBack()
        return binding.root
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode)
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.tableViewModel = viewModel
        viewModelAdapter = PreviewAdapter()
        binding.root.findViewById<RecyclerView>(R.id.recycler_view_table).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
//        viewModel.countPlayer.observe(viewLifecycleOwner, Observer { t->
//                viewModel.countPlayer.observe()
//        })
        addDecoration(binding.recyclerViewTable, context)
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                unSubscribeTopic(requireContext(), TOPIC + args.curentTable.id)
                findNavController()
                    .navigate(TableFragmentDirections.actionTableFragmentToMenuFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun navigationNext() {
        binding.buttonTbNext.setOnClickListener {
            findNavController()
//                .navigate(PreviewFragmentDirections.actionPreviewFragmentToLoginTableFragment(args.table))
        }
    }

    private fun updateApp(option: Boolean) {
        val id = args.curentTable.id.toString()
        val topic = if (!option) TOPIC + id else ROOM_TOPIC
        val send = if (!option) SEND_PLAYER else SEND_ROOM
        val value = if (!option) args.namePlayer else id
        val dataSend = mutableMapOf<String, String>()
        if (!option) dataSend[SEND_TABLE] = id
        dataSend[send] = value
        viewModel.sendUpdate(topic, dataSend) // notify server update table or player
    }

    private fun navigationNBack() {
        binding.buttonTbBack.setOnClickListener {
            unSubscribeTopic(requireContext(), TOPIC + args.curentTable.id)
            val option: Boolean = viewModel.getCountPlayer(args.curentTable.id) == 1// if stay one people in table
            val test = viewModel.getCountPlayer(args.curentTable.id)
            Log.d("countPlayer", "$test")
            viewModel.deletePlayer(args.namePlayer)
//            if (viewModel.response.value!!.isSuccessful) {
            Log.d("DELETE", "YEAH BOY")

            if (option) {
                viewModel.deleteTable(args.curentTable.id)
            }
            updateApp(option)
//            }
            findNavController()
                .navigate(TableFragmentDirections.actionTableFragmentToMenuFragment())
        }

    }
}
