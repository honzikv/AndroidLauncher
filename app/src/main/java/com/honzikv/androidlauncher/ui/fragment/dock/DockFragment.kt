package com.honzikv.androidlauncher.ui.fragment.dock

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.DockFragmentBinding
import com.honzikv.androidlauncher.model.DockItemModel
import com.honzikv.androidlauncher.ui.fragment.dock.adapter.DockAdapter
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Fragment doku umisteneho ve fragmentu plochy - HomescreenFragment
 */
class DockFragment : Fragment() {

    private val dockViewModel: DockViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    /**
     * Adapter obstaravajici zobrazeni ikon v recycler view
     */
    private lateinit var dockAdapter: DockAdapter

    /**
     * NavController pro navigaci do jinych fragmentu
     */
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DockFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    /**
     * Inicializace UI
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: DockFragmentBinding) {
        dockAdapter = DockAdapter(settingsViewModel.getShowDockLabels())
        navController = findNavController()

        //Pozorovani tematu a nastaveni pri zmene
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.cardView.setCardBackgroundColor(it.dockBackgroundColor)
            dockAdapter.setLabelColor(it.dockTextColor)
        })

        //Pozorovani zobrazeni popisku ikon
        settingsViewModel.showDockLabels.observe(viewLifecycleOwner, {
            dockAdapter.setShowLabels(it)
        })

        //Pozorovani predmetu doku a nastaveni pri zmene
        dockViewModel.dockItems.observe(viewLifecycleOwner, { itemList ->
            if (itemList.isEmpty()) {
                dockAdapter.setDockItems(mutableListOf(
                    //Nastavi "prazdny predmet" aby recycler view zustal viditelny
                    DockItemModel(position = 0, packageName = "").apply {
                        icon = ColorDrawable(Color.TRANSPARENT)
                    }
                ))
            } else {
                //Serazeni podle pozice a ulozeni do adapteru
                dockAdapter.setDockItems(itemList.toMutableList().apply { sortBy { it.position } })
            }
            dockAdapter.notifyDataSetChanged()
            binding.recyclerView.scheduleLayoutAnimation()
        })

        //Nastaveni recycler view
        binding.recyclerView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), MAX_ITEMS_IN_DOCK)
            adapter = dockAdapter
        }

    }

    /**
     * Presune se z tohoto fragmentu do [DrawerFragment]
     */
    private fun navigateToAppDrawer() {
        Timber.d("Navigating from homescreen fragment to app drawer fragment")
        navController.navigate(R.id.action_homescreenPageFragment_to_appDrawerFragment)
    }
}
