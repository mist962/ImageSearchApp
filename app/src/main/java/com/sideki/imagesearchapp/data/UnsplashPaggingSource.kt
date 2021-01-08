package com.sideki.imagesearchapp.data

import androidx.paging.PagingSource
import com.sideki.imagesearchapp.api.UnsplashApi
import retrofit2.HttpException
import java.io.IOException

/**
 *  Мы не инжектим класс потому что  переменная [query] создается динамически, после запроса на сервер а не при компиляции.
 *  1) Создаем класс [UnsplashPaggingSource] и в конструкторе добавляем переменные [UnsplashApi] и [query]
 * [query] это запрос из поисковой строки, он так же представлен и в интерефейсе [UnsplashApi].
 *  2) Наследуем класс от [PagingSource], <Int, UnsplashPhoto> где, первый параметр это тип ключа, который определяет,
 * какие данные загружать. Например. [Int] для представления номера страницы или позиции элемента или [String], если ваша
 * сеть использует строки в качестве следующих токенов, возвращаемых с каждым ответом. А второй параметр это
 * тип данных которые будут переданы в [PagingDataAdapter], для отображения в RecyclerView.
 *  3) Овверайдим метод [load], он дергает наш API запрос и загружает дату в страницы.
 *  4) Создаем переменную [position] в методе [load], она указывает на какой страницы мы находимся.
 *  5) Создаем переменную [response] в методе [load], она дергает наш API запрос, в ней мы указываем поисковый запрос,
 * номер страницы который мы хотим загрузить и количество объектов (далее этот параметр будет нами указан в [PagerConfig] в [UnsplashRepository].
 * Данный запрос полностью соответствует API запросу из [UnsplashApi].
 *  6) Создаем переменную [photos], необходимо получить List-UnsplashPhoto,
 * [response.results] это наш лист в [UnsplashResponse].
 *  7) Оборачиваем все в try-catch
 *  8) Создаем [LoadResult.Page], это одна страница в RecyclerView. [data] - это наши объекты,
 * [prevKey] - предыдущая страница ( если у нас 1 страница, то предыдущей не может быть, null),
 * [nextKey] - следуйщая страница
 */

private const val UNSPLASH_STARTING_PAGE_INDEX: Int = 1

class UnsplashPaggingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}

