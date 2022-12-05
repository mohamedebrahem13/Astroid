package com.example.astroid.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.astroid.R
import com.example.astroid.asteroidradar.repo.NetworkStatus
import com.example.astroid.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.N)
class MainFragment : Fragment(), MenuProvider {
    private var steroidAdapter: SteroidAdapter? = null
    private lateinit var binding : FragmentMainBinding



    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(
                requireActivity().application
            )
        )[MainViewModel::class.java]
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
         binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        setupRecyclerView()
        requireActivity().addMenuProvider(this,viewLifecycleOwner)

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(
                    selectedAsteroid = asteroid
                ))
                viewModel.onAsteroidDetailNavigated()
            }
        }

        viewModel.asteroids.observe(viewLifecycleOwner) {
            steroidAdapter?.submitList(it)
        }
        viewModel.networkState.observe(viewLifecycleOwner) {
            when (it) {
                NetworkStatus.LOADING -> {
                    binding.statusLoadingWheel.visibility = View.VISIBLE

                }
                else -> {
                    if (it == NetworkStatus.ERROR) {
                        Snackbar.make(requireView(), R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry") {
                                viewModel.refreshAsteroidList()
                            }
                            .show()
                    }
                    binding.statusLoadingWheel.visibility = View.GONE
                }
            }
        }
        viewModel.picState.observe(viewLifecycleOwner) {
            if (it == NetworkStatus.LOADING) {
                binding.statusLoadingWheel.visibility = View.VISIBLE

            } else {
                if (it == NetworkStatus.ERROR) {
                    Snackbar.make(
                        requireView(),
                        R.string.network_error,
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("Retry") {
                            viewModel.refreshPictureOfTheDay()
                        }
                        .show()
                }
                binding.statusLoadingWheel.visibility = View.GONE
            }
        }



        return binding.root
    }

    // set recycler view and clicked listener for asteroids
    private fun setupRecyclerView() {
        steroidAdapter= SteroidAdapter(SteroidAdapter.AsteroidListener {
            viewModel.onAsteroidClicked(it)

        })
        binding.asteroidRecycler.adapter = steroidAdapter



    }


    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater .inflate(R.menu.main_overflow_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {


        viewModel.setFilter(
            when(menuItem.itemId)
            {
                R.id.today -> Filter.TODAY
                R.id.week -> Filter.WEEK
                else -> Filter.ALL
            })

        return true
    }

}
