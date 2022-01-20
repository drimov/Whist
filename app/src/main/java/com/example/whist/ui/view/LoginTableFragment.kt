package com.example.whist.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.whist.R
import com.example.whist.databinding.FragmentLoginTableBinding
import com.example.whist.services.fcm.subscribeTopic
import com.example.whist.ui.viewmodels.login.LoginTableViewModel
import com.example.whist.utils.*
import com.example.whist.worker.retrieveDataPref
import com.example.whist.worker.saveDataLocal
import com.google.android.material.textfield.TextInputLayout


/**
 * A simple [Fragment] subclass.
 */
private const val TOPIC = TABLE_TOPIC
private const val SEND_TABLE = TABLE
private const val SEND_PLAYER = PLAYER

class LoginTableFragment : Fragment() {

    private val args by navArgs<LoginTableFragmentArgs>()
    private val viewModel: LoginTableViewModel by lazy {
        val activity = requireNotNull(this.activity) {}
        ViewModelProvider(
            this,
            LoginTableViewModel.Factory(activity.application, args)
        )[LoginTableViewModel::class.java]
    }
    private lateinit var binding: FragmentLoginTableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login_table,
            container,
            false
        )
        initBinding()
        onBackPressed()
        navigationNext()
        navigationBack()
        listenerUser(false)
        if (args.table.isPrivate) listenerPwd(true)

        return binding.root
    }

    private fun updatePlayers(user: String, tableId: String, topic:String) {
        val dataSend = mutableMapOf<String, String>()
        dataSend[SEND_TABLE] = tableId
        dataSend[SEND_PLAYER] = user
        viewModel.sendUpdate(topic, dataSend) // notify server update player list
    }

    private fun dataPush(user: String) {
        viewModel.stateLoading()
        val tokenName = activity?.getString(R.string.token_key)
        val token = activity?.let { retrieveDataPref(it.applicationContext, tokenName!!) }
        val fcmId = viewModel.getFcm(token)
        viewModel.insertPlayer(user, args.table.id, fcmId)
        if (viewModel.response.value!!.isSuccessful) {
            viewModel.stateLoading()
            val id = args.table.id.toString()
            val newTopic = TOPIC + id
            subscribeTopic(requireContext(), newTopic) // subscribe to table
            val topicName = activity?.getString(R.string.topic) // add to pref file
            saveDataLocal(requireContext(), newTopic, topicName!!)
            updatePlayers(user, id, newTopic)
            //go next
            findNavController()
                .navigate(
                    LoginTableFragmentDirections.actionLoginTableFragmentToTableFragment(
                        args.table,
                        user
                    )
                )
        }   // Problem
    }

    private fun formControl() {
        val option = binding.logTable!!.table.isPrivate
        val arrayContainer = ArrayList<TextInputLayout>()
        arrayContainer.apply {
            this.add(binding.containerUserLog)
            if (option) this.add(binding.containerPwdLog)
        }
        val arrayEditText = ArrayList<EditText>()
        arrayEditText.apply {
            this.add(binding.editTextUserLog)
            if (option) this.add(binding.editTextPwdLog)
        }
        val result = submitFormLog(
            context,
            arrayContainer,
            arrayEditText,
            option
        )
        if (!result) {
            invalidFormLog(context, arrayContainer, option)
        } else {
            // Retrieve data
            val user = arrayEditText[0].text.toString()
            var pwd: String = ""

            if (option) { // pwd exist?
                pwd = arrayEditText[1].text.toString()
            }
            val userIsAvailable = viewModel.nameIsAvailable(user)

            if (userIsAvailable) {
                if (option && viewModel.pwdIsCorrect(pwd) || !option) {
                    dataPush(user)
                } else resetFormLog(context, true) // Error pwd didn't match
            } else {
                resetFormLog(context, false) // Error name exist
            }
        }

    }

    private fun listenerUser(option: Boolean) {
        focusListener(context, binding.editTextUserLog, binding.containerUserLog, option)
    }

    private fun listenerPwd(option: Boolean) {
        focusListener(context, binding.editTextPwdLog, binding.containerPwdLog, option)
    }

    private fun initBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.logTable = viewModel

    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController()
                    .navigate(LoginTableFragmentDirections.actionLoginTableFragmentToRoomFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun navigationNext() {
        binding.buttonLogTableNext.setOnClickListener {
            formControl()
        }
    }

    private fun navigationBack() {
        binding.buttonLogTableBack.setOnClickListener {
            findNavController()
                .navigate(LoginTableFragmentDirections.actionLoginTableFragmentToRoomFragment())
        }

    }
}