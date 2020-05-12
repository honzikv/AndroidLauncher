package com.honzikv.androidlauncher.ui.fragment.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.dialog.adapter.EditDockAdapter
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditDockItemsDialogFragment private constructor() : DialogFragment() {

    private val dockViewModel: DockViewModel by viewModel()

    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var dockItemAdapter: EditDockAdapter

    companion object {
        fun newInstance() = EditDockItemsDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EditHomescreenContainerFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        dockItemAdapter = EditDockAdapter { dockViewModel.removeItem(it) }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                cardViewPageHeader.setCardBackgroundColor(cardViewBackgroundColor)
                cardViewRecyclerView.setCardBackgroundColor(cardViewBackgroundColor)
                constraintLayout.setBackgroundColor(backgroundColor)

                dockItemAdapter.setTextColor(textFillColor)
                dockItemAdapter.notifyDataSetChanged()

                addButton.setColorFilter(textFillColor)
                deleteButton.setColorFilter(textFillColor)
                okButton.setColorFilter(textFillColor)
                containerName.setTextColor(textFillColor)
                itemCountText.setTextColor(textFillColor)
            }
        })

        binding.deleteButton.visibility = View.GONE
        binding.containerName.text = "Dock"

        dockViewModel.dockItems.observe(viewLifecycleOwner, {
            val itemCountText = "${it.size} / $MAX_ITEMS_IN_DOCK items"
            binding.itemCountText.text = itemCountText

            dockItemAdapter.setItemList(it)
            dockItemAdapter.notifyDataSetChanged()
        })

        //Observe for error when new apps are getting uploaded
        dockViewModel.dockPostError.observe(viewLifecycleOwner, { event ->
            Timber.d("printing message")
            event.getContentIfNotHandledOrReturnNull()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        })

        binding.itemListRecyclerView.apply {
            adapter = dockItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.okButton.setOnClickListener { dismiss() }
        binding.addButton.setOnClickListener {
            AppPickerDialogFragment.newInstance(AppPickerDialogFragment.getDockFolderId())
                .show(requireActivity().supportFragmentManager, "editDockApps")
        }
    }
}