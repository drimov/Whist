package com.example.whist.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whist.R
import com.example.whist.data.domain.TableGame
import com.example.whist.databinding.FragmentRoomBinding
import com.example.whist.services.fcm.subscribeTopic
import com.example.whist.services.fcm.unSubscribeTopic
import com.example.whist.ui.viewmodels.room.RoomAdapter
import com.example.whist.ui.viewmodels.room.RoomViewModel
import com.example.whist.utils.ROOM
import com.example.whist.utils.ROOM_TOPIC
import com.example.whist.utils.TABLE_TOPIC
import com.example.whist.utils.VerticalSpacingItemDecorator
import com.example.whist.worker.deleteDataPref
import com.example.whist.worker.saveDataLocal
import kotlin.properties.Delegates


/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */

private const val TOPIC = ROOM_TOPIC
private const val WHERE_SEND = ROOM

class RoomFragment : Fragment(), RoomAdapter.OnItemClickListener {

    private var verticalSpacingItemDecoratorValue by Delegates.notNull<Int>()

    private val viewModel: RoomViewModel by lazy {
        val activity = requireNotNull(this.activity) {

        }
        ViewModelProvider(
            this,
            RoomViewModel.Factory(activity.application)
        )[RoomViewModel::class.java]
    }
    private var viewModelAdapter: RoomAdapter? = null
    private lateinit var binding: FragmentRoomBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.room.observe(viewLifecycleOwner, { tables ->
            tables?.apply {
                viewModelAdapter?.room = tables
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        verticalSpacingItemDecoratorValue = resources.getInteger(R.integer.vertical_spacing_value)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_room,
            container,
            false
        )
        initBinding()
        binding.roomViewModel!!.room.observe(viewLifecycleOwner) {
            binding.recyclerViewRoom.invalidate()
        }
        addDecoration()
        subscribeRoom()
        onBackPressed()
        return binding.root
    }

    private fun subscribeRoom() {
        subscribeTopic(requireContext(), TOPIC)
        val topicName = requireContext().getString(R.string.topic)
        saveDataLocal(requireContext(), TOPIC, topicName)
    }

    private fun unSubscribeRoom() {
        unSubscribeTopic(requireContext(), TOPIC)
        val topicName = requireContext().getString(R.string.topic)
        deleteDataPref(requireContext(), topicName)
//        unSubscribeTopic(requireContext(),DEL+43)
//        unSubscribeTopic(requireContext(),DEL+44)
//        unSubscribeTopic(requireContext(),DEL+45)
//        unSubscribeTopic(requireContext(),DEL+46)
//        unSubscribeTopic(requireContext(),DEL+75)
//        unSubscribeTopic(requireContext(),DEL+76)
//        unSubscribeTopic(requireContext(),DEL+77)

    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                unSubscribeRoom()

                findNavController()
                    .navigate(RoomFragmentDirections.actionRoomFragmentToMenuFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner, callback)

    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.roomViewModel = viewModel
        binding.recyclerViewRoom.adapter = RoomAdapter(this)  // Add onClickListener
        viewModelAdapter = binding.recyclerViewRoom.adapter as RoomAdapter // Link to Adapter
        binding.root.findViewById<RecyclerView>(R.id.recycler_view_room).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = viewModelAdapter
        }
    }

    private fun addDecoration() {
        binding.recyclerViewRoom.addItemDecoration(
            DividerItemDecoration(
                this.activity,
                LinearLayout.VERTICAL
            )
        )
        binding.recyclerViewRoom.addItemDecoration(
            VerticalSpacingItemDecorator(
                verticalSpacingItemDecoratorValue
            )
        )
    }

    override fun onItemClick(position: Int) {
//        Log.i("onItemClick", "$position")

//        viewModelAdapter?.notifyItemChanged(position)
        val clickedItem = viewModel.room.value

        val table: TableGame? = clickedItem?.get(position)
//        val tableId = clickedItem?.get(position)?.id
        unSubscribeRoom()
        findNavController()
            .navigate(RoomFragmentDirections.actionRoomFragmentToPreviewFragment(table!!))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        binding.roomViewModel?.room?.removeObservers(viewLifecycleOwner)
        viewModelAdapter = null
    }
}