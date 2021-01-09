package com.sideki.imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.sideki.imagesearchapp.R
import com.sideki.imagesearchapp.api.UnsplashApi
import com.sideki.imagesearchapp.data.UnsplashPhoto
import com.sideki.imagesearchapp.data.UnsplashRepository
import com.sideki.imagesearchapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * 1) .withLoadStateHeaderAndFooter добавляется к адапретру после создания LoadStateAdapter
 * 2) Функция onCreateOptionsMenu добавляется после создания menu_gallery.xml
 * 3) Добавляем setHasOptionMenu(true)
 * 4) adapter.addLoadStateListener{} Добавляем после функции поиска
 * 5) Добавляем buttonRetry.setOnClickListener
 *
 * ---------Клики в ресайклере-----------
 * 1) Имплементируем UnsplashPhotoAdapter.OnItemClickListener
 * 2) Добавляем в конструктов adapter this
 * 2) Овверайдим onItemClick
 */

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), UnsplashPhotoAdapter.OnItemClickListener
     {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)
        setHasOptionsMenu(true)

        val adapter = UnsplashPhotoAdapter(this)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() },
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        adapter.addLoadStateListener { loadStates ->
            binding.apply {
                progress_bar.isVisible = loadStates.source.refresh is LoadState.Loading
                recycler_view.isVisible = loadStates.source.refresh is LoadState.NotLoading
                button_retry.isVisible = loadStates.source.refresh is LoadState.Error
                text_view_error.isVisible = loadStates.source.refresh is LoadState.Error

                //empty view
                if (loadStates.source.refresh is LoadState.NotLoading &&
                    loadStates.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    recycler_view.isVisible = false
                    text_view_empty.isVisible = true
                } else {
                    text_view_empty.isVisible = false
                }
            }

        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView //Обязательно выбирать из androidx

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus() // убирает клавиатуру
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

}