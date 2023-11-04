package com.example.track.ui.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.track.R
import com.example.track.adapter.RunAdapter
import com.example.track.databinding.FragmentRunBinding
import com.example.track.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.track.other.SortType.AVG_SPEED
import com.example.track.other.SortType.CALORIES_BURNED
import com.example.track.other.SortType.DATE
import com.example.track.other.SortType.DISTANCE
import com.example.track.other.SortType.RUNNING_TIME
import com.example.track.other.TrackingUtility
import com.example.track.other.TrackingUtility.hasLocationPermissions
import com.example.track.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var runAdapter: RunAdapter
    private val binding by lazy { FragmentRunBinding.inflate(LayoutInflater.from(requireContext())) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    private fun setupRecyclerView() = binding.rvRuns.apply {
        runAdapter = RunAdapter(requireContext())
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        setupRecyclerView()


        when (viewModel.sortType) {
            DATE -> binding.spFilter.setSelection(0)
            RUNNING_TIME -> binding.spFilter.setSelection(1)
            DISTANCE -> binding.spFilter.setSelection(2)
            AVG_SPEED -> binding.spFilter.setSelection(3)
            CALORIES_BURNED -> binding.spFilter.setSelection(4)
        }

        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> viewModel.sortRuns(DATE)
                    1 -> viewModel.sortRuns(RUNNING_TIME)
                    2 -> viewModel.sortRuns(DISTANCE)
                    3 -> viewModel.sortRuns(AVG_SPEED)
                    4 -> viewModel.sortRuns(CALORIES_BURNED)

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })


        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }
    }

    private fun requestPermissions() {
        if (hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}