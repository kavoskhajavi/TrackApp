package com.example.track.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.track.R
import com.example.track.databinding.FragmentRunBinding
import com.example.track.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val binding by lazy { FragmentSettingsBinding.inflate(LayoutInflater.from(requireContext())) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }


}