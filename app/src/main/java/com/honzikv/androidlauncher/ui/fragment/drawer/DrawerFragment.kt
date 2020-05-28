package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.model.ThemeProfileModel

import com.honzikv.androidlauncher.databinding.AppDrawerFragmentBinding
import com.honzikv.androidlauncher.utils.RADIUS_CARD_VIEW
import com.honzikv.androidlauncher.ui.fragment.drawer.adapter.AppDrawerAdapter
import com.honzikv.androidlauncher.utils.DRAWER_GRID_COLUMNS
import com.honzikv.androidlauncher.utils.applyAlpha
import com.honzikv.androidlauncher.viewmodel.DrawerViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Fragment pro Drawer se vsemi aplikacemi
 */
class DrawerFragment : Fragment() {

    companion object {
        const val OVERSCROLL_VAL = -160
    }

    private val drawerViewModel: DrawerViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    /**
     * Adapter pro vytvoreni jednotlivych ikon
     */
    private lateinit var appDrawerAdapter: AppDrawerAdapter

    /**
     * NavController pro navigaci do jinych fragmentu
     */
    private lateinit var navController: NavController

    private lateinit var binding: AppDrawerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("creating view")
        navController = findNavController()
        binding =
            AppDrawerFragmentBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    /**
     * Inicializace UI
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initialize() {
        appDrawerAdapter = AppDrawerAdapter()

        binding.appDrawerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appDrawerAdapter
        }

        binding.searchView.apply {
            //Nastavni filter v searchView pro vyhledavani
            setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    appDrawerAdapter.filter.filter(newText)
                    return false
                }
            })
            setBackgroundColor(Color.TRANSPARENT)
        }

        //Nastavi ikonu pro navigaci do fragmentu s nastavenim
        binding.settingsIcon.setOnClickListener { navigateToSettings() }

        //Nastavi ikonu pro navigaci zpet na plochu
        binding.backButton.setOnClickListener { navigateToHomescreen() }

        //Pozorovani zda-li se maji pouzivat zakulacene rohy
        settingsViewModel.useRoundCorners.observe(viewLifecycleOwner, { use ->
            useRoundCardView(binding, use)
        })

        //Pozorovani pro aktualizaci barev
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            updateTheme(binding, theme)
        })

        //Pozorovani pro pouziti mrizky misto linearniho seznamu
        settingsViewModel.showDrawerAsGrid.observe(viewLifecycleOwner, { useGrid ->
            useDrawerAsGrid(binding, useGrid)
            appDrawerAdapter.useGrid = useGrid
            appDrawerAdapter.notifyDataSetChanged()
        })

        //Pozorovani seznamu vsech aplikaci a aktualizace pri zmene
        drawerViewModel.getDrawerApps().observe(viewLifecycleOwner, {
            appDrawerAdapter.updateData(it)
            appDrawerAdapter.notifyDataSetChanged()
        })

        //Pozorovani zda-li se ma vyhledavani zobrazit
        settingsViewModel.showSearchBar.observe(viewLifecycleOwner, { show ->
            if (show) {
                binding.searchView.visibility = View.VISIBLE
                binding.searchCardView.visibility = View.VISIBLE
            } else {
                binding.searchCardView.visibility = View.GONE
                binding.searchView.visibility = View.GONE
            }
        })
    }

    /**
     * Zmeni layout manager na GridLayoutManager pokud [use] je true, jinak na LinearLayoutManager
     */
    private fun useDrawerAsGrid(binding: AppDrawerFragmentBinding, use: Boolean) {
        binding.appDrawerRecyclerView.layoutManager =
            if (use) {
                GridLayoutManager(requireContext(), DRAWER_GRID_COLUMNS)
            } else {
                LinearLayoutManager(requireContext())
            }
    }

    /**
     * Aktualizuje barvy UI
     */
    private fun updateTheme(binding: AppDrawerFragmentBinding, theme: ThemeProfileModel) {
        appDrawerAdapter.setLabelColor(theme.drawerTextFillColor)

        binding.apply {
            constraintLayout.setBackgroundColor(applyAlpha(theme.drawerTextFillColor, 120))
            appDrawerCardView.setCardBackgroundColor(theme.drawerBackgroundColor)
            searchCardView.setCardBackgroundColor(theme.drawerSearchBackgroundColor)
            searchView.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
            settingsIcon.setColorFilter(theme.drawerBackgroundColor)
            backButton.setColorFilter(theme.drawerBackgroundColor)
            allApps.setTextColor(theme.drawerBackgroundColor)
        }
    }

    /**
     * Nastavi zakulacene rohy pokud je [use] true
     */
    private fun useRoundCardView(binding: AppDrawerFragmentBinding, use: Boolean) {
        if (use) {
            binding.appDrawerCardView.radius =
                RADIUS_CARD_VIEW
            binding.searchCardView.radius =
                RADIUS_CARD_VIEW
        } else {
            binding.appDrawerCardView.radius = 0f
            binding.searchCardView.radius = 0f
        }
    }

    /**
     * Presune se na fragment s domovskou obrazovkou
     */
    private fun navigateToHomescreen() =
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)

    /**
     * Presune se na fragment s nastavenim
     */
    private fun navigateToSettings() =
        navController.navigate(R.id.action_appDrawerFragment_to_settingsFragment)


}
