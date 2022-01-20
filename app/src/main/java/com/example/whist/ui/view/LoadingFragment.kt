package com.example.whist.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import com.example.whist.R
import com.example.whist.utils.navigationWithoutButton
import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */




class LoadingFragment : Fragment() {

    private var DELAY_LOAD by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DELAY_LOAD = this.resources.getInteger(R.integer.delay_load).toLong()

        Handler(Looper.getMainLooper()).postDelayed({
            navigationNext(view)
        }, DELAY_LOAD)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    private fun navigationNext(view: View){
        view.findNavController()
            .navigate(LoadingFragmentDirections.actionLoadingFragmentToHomeFragment())
    }
}