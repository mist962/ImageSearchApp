package com.sideki.imagesearchapp.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sideki.imagesearchapp.R
import com.sideki.imagesearchapp.data.UnsplashPhoto
import com.sideki.imagesearchapp.databinding.ItemUnspalshPhotoBinding

/**
 * 1) Первый параметр это то что мы хотим передать в RecyclerView, второй папарметр это вью холдер который мы хотим передать
 * 2) В конструкторе [PhotoViewHolder], private val binding: [ItemUnspalshPhotoBinding], [ItemUnspalshPhotoBinding] это автогенерируемый
 * файн от ViewBinding
 * 3) Создаем companion object и в нем создаем [object] наследуемый от [DiffUtil.ItemCallback] ( кажется он проверяет
 * необходимо обновлять объект в ресайклере)
 * 4) [id] это поле в [UnsplashPhoto]
 * 5) оверайдим методы [UnsplashPhotoAdapter]
 * 6) инфлейтим [onCreateViewHolder]
 * 7) создаем метод [bind] в [PhotoViewHolder]
 * 8) байндим в [onBindViewHolder]
 * 9) Добавляем [PHOTO_COMPARATOR] в конструктор UnsplashPhotoAdapter.PhotoViewHolder>()
 * 10) Инициализируе адаптер в фрагменте
 *
 * -------------------------------Клики-----------------------------
 * 11) Создаем interface [onItemClickListener] и добавляем его в конструктор [UnsplashPhotoAdapter]
 * 12) Создаем блок init{} в [PhotoViewHolder], и добавляем слушатель кликов для того чтобы можно было уйти в детали объекта
 * 13) Реализуем клик а GalleryFragment
 *
 */

class UnsplashPhotoAdapter(val listener: OnItemClickListener) :
    PagingDataAdapter<UnsplashPhoto, UnsplashPhotoAdapter.PhotoViewHolder>(PHOTO_COMPARATOR) {

    inner class PhotoViewHolder(private val binding: ItemUnspalshPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) { // Эта проверка необходима на случай если пользователь нажмет на объект во время анимации (например удаление по свайпу)
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(photo: UnsplashPhoto) {
            binding.apply {
                Glide.with(itemView).load(photo.urls.regular).centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)
                textViewUserName.text = photo.user.username
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PhotoViewHolder(
        ItemUnspalshPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val curItem = getItem(position)

        if (curItem != null) {
            holder.bind(curItem)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(photo: UnsplashPhoto)
    }

    companion object {
        private val PHOTO_COMPARATOR = object : DiffUtil.ItemCallback<UnsplashPhoto>() {
            override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto) =
                oldItem == newItem
        }
    }
}