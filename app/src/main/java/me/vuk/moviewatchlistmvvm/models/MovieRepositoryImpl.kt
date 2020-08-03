package me.vuk.moviewatchlistmvvm.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.vuk.moviewatchlistmvvm.db
import me.vuk.moviewatchlistmvvm.db.MovieDao
import me.vuk.moviewatchlistmvvm.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class MovieRepositoryImpl : MovieRepository {
    private val movieDao: MovieDao = db.movieDao()
    private val retrofitClient = RetrofitClient()
    private val allMovies: LiveData<List<Movie>>

    init {
        allMovies = movieDao.getAllMovies()
    }

    override fun getSavedMovies(): LiveData<List<Movie>> {
        return allMovies
    }

    override fun saveMovie(movie: Movie) {
        thread {
            movieDao.insert(movie)
        }
    }

    override fun deleteMovie(movie: Movie) {
        thread {
            movieDao.delete(movie.id)
        }
    }

    override fun searchMovies(query: String): LiveData<List<Movie>?> {
        val data = MutableLiveData<List<Movie>>()

        retrofitClient.searchMovies(query).enqueue(object: Callback<MoviesResponse>{
            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                data.value = null
                Log.e(this.javaClass.simpleName, "Failed response")
            }

            override fun onResponse(
                call: Call<MoviesResponse>,
                response: Response<MoviesResponse>
            ) {
                data.value = response.body()?.movies
                Log.e(this.javaClass.simpleName, "Response ${response.body()?.movies}")
            }
        })
        return data
    }
}