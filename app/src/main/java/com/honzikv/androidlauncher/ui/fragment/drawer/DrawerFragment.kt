package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.model.ThemeProfileModel

import com.honzikv.androidlauncher.databinding.AppDrawerFragmentBinding
import com.honzikv.androidlauncher.utils.RADIUS_CARD_VIEW
import com.honzikv.androidlauncher.ui.fragment.drawer.adapter.AppDrawerAdapter
import com.honzikv.androidlauncher.utils.gestures.OnSwipeTouchListener
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

        //Nastavni filter v searchView pro vyhledavani
        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                appDrawerAdapter.filter.filter(newText)
                return false
            }
        })

        //Nastavi gesta pro navigaci zpet
        binding.constraintLayout.setOnTouchListener(object :
            OnSwipeTouchListener(binding.root.context) {
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                navigateToHomescreenFragment()
            }
        })

        //Nastavi ikonu pro navigaci do fragmentu s nastavenim
        binding.settingsIcon.setOnClickListener {
            navigateToSettings()
        }

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
            createOverScrollLayoutManager(requireContext(), use)
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
    private fun navigateToHomescreenFragment() =
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)

    /**
     * Presune se na fragment s nastavenim
     */
    private fun navigateToSettings() =
        navController.navigate(R.id.action_appDrawerFragment_to_settingsFragment)

    /**
     * Nastavi LayoutManageru moznost detekovat "overscroll" abychom mohli drawer zavrit i tazenim
     * nahoru pokud jsme na zacatku
     */
    private fun createOverScrollLayoutManager(
        context: Context,
        useGridLayoutManager: Boolean
    ): RecyclerView.LayoutManager {
        if (!useGridLayoutManager) {
            return object : LinearLayoutManager(context) {
                override fun scrollVerticallyBy(
                    dy: Int,
                    recycler: RecyclerView.Recycler?,
                    state: RecyclerView.State?
                ): Int {
                    val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
                    //Pro < 0 se zavre prilis snadno - uzivatel by mohl zavrit i omylem
                    if (dy - scrollRange < -60) {
                        navigateToHomescreenFragment()
                    }
                    return scrollRange
                }
            }
        }

        return object : GridLayoutManager(context, DRAWER_GRID_COLUMNS) {
            override fun scrollVerticallyBy(
                dy: Int,
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ): Int {
                val scrollRange = super.scrollVerticallyBy(dy, recycler, state)
                if (dy - scrollRange < -60) {
                    Timber.d("User overscrolled")
                    navigateToHomescreenFragment()
                }
                return scrollRange
            }
        }
    }

}
