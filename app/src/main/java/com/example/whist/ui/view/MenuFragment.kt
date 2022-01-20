package com.example.whist.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.whist.MainActivity
import com.example.whist.R
import com.example.whist.utils.clearDatabase
import com.example.whist.utils.navigationWithoutButton

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val btnRoomView: Button = view.findViewById(R.id.buttonRoom)
        val btnTableCreate: Button = view.findViewById(R.id.buttonCreateTable)


        btnRoomView.setOnClickListener {
            view.findNavController()
                .navigate(MenuFragmentDirections.actionMenuFragmentToRoomFragment())
        }
        btnTableCreate.setOnClickListener {
            view.findNavController()
                .navigate(MenuFragmentDirections.actionMenuFragmentToCreateTableFragment())
        }
        onBackPressed()
        // Inflate the layout for this fragment
        return view
    }

    private fun onBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController()
                    .navigate(MenuFragmentDirections.actionMenuFragmentToHomeFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}