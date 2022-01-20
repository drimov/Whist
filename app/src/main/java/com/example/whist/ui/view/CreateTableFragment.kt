package com.example.whist.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.collection.ArrayMap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.work.CoroutineWorker
import com.example.whist.R
import com.example.whist.databinding.FragmentCreateTableBinding
import com.example.whist.services.fcm.subscribeTopic
import com.example.whist.ui.viewmodels.createTable.CreateTableViewModel
import com.example.whist.utils.*
import com.example.whist.worker.retrieveDataPref
import com.example.whist.worker.saveDataLocal
import com.google.android.material.textfield.TextInputLayout
import okhttp3.RequestBody


/**
 * A simple [Fragment] subclass.
 */
private const val TOPIC = TABLE_TOPIC
private const val TOPIC_UPDATE = ROOM_TOPIC
//private const val WHERE_SEND = ROOM
private const val SEND_ROOM = ROOM

class CreateTableFragment : Fragment() {

    private val viewModel: CreateTableViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(
            this,
            CreateTableViewModel.Factory(activity.application)
        )[CreateTableViewModel::class.java]
    }
    private lateinit var binding: FragmentCreateTableBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_create_table,
            container,
            false
        )
        initBinding()
        switchToggle()
        listenerUser(false)
        listenerTable(false)
        navigationNext()
        navigationBack()
        return binding.root
    }

    private fun initBinding() {
        binding.lifecycleOwner = this
        binding.createTable = viewModel
    }

    private fun switchToggle() {
        binding.switchPrivacy.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changePrivacy()
            Log.d("isChecked", "$isChecked")
            if (isChecked) listenerPwd(true)
        }
    }

    private fun dataPush(user: String, table: String, password: String) {
        viewModel.stateLoading()
//        if (viewModel.nameIsAvailable(user)) {
//            if (viewModel.tableIsAvailable(table)) {
        viewModel.insertTable(table, password)
        if (viewModel.response.value!!.isSuccessful) {

            val tableId = viewModel.getTable(table)
            val table = viewModel.getTableById(tableId)
            val tokenName = activity?.getString(R.string.token_key)
            val token = activity?.let { retrieveDataPref(it.applicationContext, tokenName!!) }
            val fcmId = viewModel.getFcm(token)

            viewModel.insertPlayer(user, tableId, fcmId)
            if (viewModel.response.value!!.isSuccessful) {
                Log.d("player", "insert ok")
                viewModel.stateLoading()
                val newTopic = TOPIC + tableId.toString()
                val dataSend = mutableMapOf<String, String>()
                dataSend[SEND_ROOM] = ""
                subscribeTopic(requireContext(), newTopic)// subscribe to new table
                val topicName = activity?.getString(R.string.topic) // add to pref file
                saveDataLocal(requireContext(), newTopic, topicName!!)
                viewModel.sendUpdate(TOPIC_UPDATE, dataSend) // notify server update room

                findNavController()
                    .navigate(
                        CreateTableFragmentDirections.actionCreateTableFragmentToTableFragment(
                            table,
                            user
                        )
                    )
            }
        }
//                if (pushTable(table,password) && pushFcm(token)){
//                    val tableId = viewModel.getTable(table)
//                    val fcmId = viewModel.getFcm(token)
//                    pushPlayer(user, tableId, fcmId)
//                }else{
//                    Log.d("table or fcm error", "-_-")
//                }
//            } else Log.d("table", "is not available")
//        } else Log.d("name", "is not available")
    }

    private fun formControl() {
        val option = binding.createTable?.privacy?.value!!
        val arrayContainer = ArrayList<TextInputLayout>()
        arrayContainer.apply {
            this.add(binding.containerUserCreate)
            this.add(binding.containerTable)
            if (option) this.add(binding.containerPwdCreate)
        }
        val arrayEditText = ArrayList<EditText>()
        arrayEditText.apply {
            this.add(binding.editTextUserCreate)
            this.add(binding.editTextTable)
            if (option) this.add(binding.editTextCreatePwd)
        }
        val result = submitFormCreate(
            context,
            arrayContainer,
            arrayEditText,
            option
        )
        if (!result) {
            invalidFormCreate(context, arrayContainer, option)
        } else {
            // Retrieve data
            val user = arrayEditText[0].text.toString()
            val table = arrayEditText[1].text.toString()
            var pwd: String = ""

            if (option) {
                pwd = arrayEditText[2].text.toString()
            }
            val userIsAvailable = viewModel.nameIsAvailable(user)
            val tableIsAvailable = viewModel.tableIsAvailable(table)

            if (userIsAvailable && tableIsAvailable) {
                dataPush(user, table, pwd)
            } else {
                val arrayAvailable = ArrayList<Boolean>()
                arrayAvailable.add(userIsAvailable)
                arrayAvailable.add(tableIsAvailable)
                resetFormCreate(context, arrayAvailable)
            }
        }
    }

    private fun listenerUser(option: Boolean) {
        focusListener(context, binding.editTextUserCreate, binding.containerUserCreate, option)
    }

    private fun listenerTable(option: Boolean) {
        focusListener(context, binding.editTextTable, binding.containerTable, option)
    }

    private fun listenerPwd(option: Boolean) {
        focusListener(context, binding.editTextCreatePwd, binding.containerPwdCreate, option)
    }

    private fun navigationNext() {
        binding.buttonCreateNext.setOnClickListener {
            formControl()
//            viewModel.sendUpdate(TOPIC_UPDATE, DATA)

        }
    }

    private fun navigationBack() {
        binding.buttonCreateBack.setOnClickListener {
            findNavController()
                .navigate(CreateTableFragmentDirections.actionCreateTableFragmentToMenuFragment())
        }

    }
}