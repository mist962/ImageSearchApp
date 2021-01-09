package com.sideki.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sideki.imagesearchapp.databinding.UnsplashPhotoLoadStateFooterBinding

/**
 * 1) Наследуем класс от [LoadStateAdapter], ему потребуется ViewHolder
 * 2) Создаем [LoadStateViewHolder], [UnsplashPhotoLoadStateFooterBinding] - автоматически генерируется из ViewBinding
 * 3) Овверайдим методы [LoadStateAdapter]
 * 4) Инфлейтим [onCreateViewHolder]
 * 5) Создаем метод [bind] в [LoadStateViewHolder]
 * 6) Вызываем метод [bind] в [onBindViewHolder]
 * 7) Добавляет onClick к [buttonRetry] в init{} блоке [LoadStateViewHolder]
 * 8) Добавляем private val retry: () -> Unit в конструктор [UnsplashPhotoLoadStateAdapter].
 * Через конструктор будет передаваться функция для обновления загрузки, при нажатии кнопки Retry
 * 9) Далее в [GalleryFragment] коннектим этот адаптер
 */

class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnsplashPhotoLoadStateAdapter.LoadStateViewHolder>() {

    inner class LoadStateViewHolder(private val binding: UnsplashPhotoLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                buttonRetry.isVisible = loadState !is LoadState.Loading
                textViewError.isVisible = loadState !is LoadState.Loading
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) = LoadStateViewHolder(
        UnsplashPhotoLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
    )


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


}