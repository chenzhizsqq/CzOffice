package com.example.etoffice.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.etoffice.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val textTitle1: TextView = root.findViewById(R.id.text_title1)
        homeViewModel.title1.observe(viewLifecycleOwner, Observer {
            textTitle1.text = it
        })

        val textTitle2: TextView = root.findViewById(R.id.text_title2)
        homeViewModel.title2.observe(viewLifecycleOwner, Observer {
            textTitle2.text = it
        })

        val textTitle3: TextView = root.findViewById(R.id.text_title3)
        homeViewModel.title3.observe(viewLifecycleOwner, Observer {
            textTitle3.text = it
        })

        val textTitle4: TextView = root.findViewById(R.id.text_title4)
        homeViewModel.title4.observe(viewLifecycleOwner, Observer {
            textTitle4.text = it
        })
        return root
    }
}