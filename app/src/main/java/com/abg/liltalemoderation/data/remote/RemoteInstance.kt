package com.abg.liltalemoderation.data.remote

import android.content.Context
import android.widget.ImageView
import com.abg.liltalemoderation.R
import com.abg.liltalemoderation.data.remote.service.ComplaintService
import com.abg.liltalemoderation.data.remote.service.FileService
import com.abg.liltalemoderation.data.remote.service.PostService
import com.abg.liltalemoderation.model.pojo.User
import com.google.gson.GsonBuilder
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RemoteInstance {

    var user: User = User( "", "")
        private set

    fun setUser(user: User) {
        RemoteInstance.user = user
    }

    private const val baseUrl: String = "http://10.0.2.2:8090/" //"http://82.97.248.120:8090/"

    //"http://10.0.2.2:8090/"

    private val interceptorBody: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun basicAuthInterceptor(username: String, password: String): Interceptor {
        return BasicAuthInterceptor(username, password)
    }

    private fun client(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(interceptorBody)
            .build()

    private val gson = GsonBuilder().setLenient().create()

    private fun retrofitBase(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))

    private fun retrofitWithAuth(): Retrofit.Builder =
        retrofitBase().client(client(basicAuthInterceptor(user.username, user.password)))

    fun setPicasso(context: Context) {
        try {
            val picasso = Picasso.Builder(context)
                .memoryCache(LruCache(context))
                .downloader(
                    OkHttp3Downloader(
                        client(basicAuthInterceptor(user.username, user.password))
                    )
                ).build()
            Picasso.setSingletonInstance(picasso)
        } catch (_: IllegalStateException) {
        }

    }

    fun apiIcon(imageView: ImageView, userId: Long, invalidate: Boolean) {

        val picasso = Picasso.get()

        if (invalidate) {
            picasso.invalidate("${baseUrl}api-v1/files/icon/search?userId=$userId")
        } else {
            Picasso.get()
                .load("${baseUrl}api-v1/files/icon/search?userId=$userId")
                .error(R.drawable.account_circle_fill0_wght200_grad0_opsz24)
                .into(imageView)
        }
    }

    fun apiImage(imageView: ImageView, imageId: Long?) {
        Picasso.get()
            .load("${baseUrl}api-v1/files/image/search?id=$imageId")
            .into(imageView)
    }

    fun apiPost(): PostService =
        retrofitWithAuth().build().create(PostService::class.java)

    fun apiFileHandle(): FileService =
        retrofitWithAuth().build().create(FileService::class.java)

    fun apiComplaintReport(): ComplaintService =
        retrofitWithAuth().build().create(ComplaintService::class.java)

}