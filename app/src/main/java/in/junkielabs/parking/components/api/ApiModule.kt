package `in`.junkielabs.parking.components.api

import `in`.junkielabs.parking.BuildConfig
import `in`.junkielabs.parking.application.ApplicationMy
import `in`.junkielabs.parking.components.api.internal.MethodRequestCache
import `in`.junkielabs.parking.components.api.routepoints.ApiPointAuth
import `in`.junkielabs.parking.components.api.routepoints.ApiPointGuard
import android.app.Application
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by Niraj on 19-10-2021.
 */
object ApiModule {
    private var retrofit: Retrofit

    init {
        retrofit = provideRetrofit(
            provideOkHttpClient(
                provideCacheInterceptor(),
                provideCache(ApplicationMy.instance)
            ),
            provideBaseUrl()
        )
    }

    private fun provideBaseUrl() = BuildConfig.API_URL

    private fun provideCacheInterceptor() = Interceptor { chain ->
        var request = chain.request()
        //LOGW(TAG, "cache interceptor offline")
        if (!ApplicationMy.hasNetwork()) {
            // LOGW(TAG, "cache interceptor offline2")
            val cacheControl = CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .cacheControl(cacheControl)
                .removeHeader("Authorization")
                .build()
        }

        chain.proceed(request)
    }

    private fun provideCache(application: Application) = try {
        Cache(
            File(application.cacheDir, "http-cache"),
            10 * 1024 * 1024
        ) // 10 MB
    } catch (e: Exception) {
        null
//                info(e.toString() + "Could not create Cache!")
//                FirebaseCrash.report(e)
    }

    fun provideMethodCache() = MethodRequestCache()


    private fun provideOkHttpClient(cacheInterceptor: Interceptor, cache: Cache?): OkHttpClient {


        val httpClient = OkHttpClient.Builder()
        // TODO getUnsafeOkHttpClient(httpClient)

        httpClient.connectTimeout(55, TimeUnit.SECONDS)
        httpClient.readTimeout(55, TimeUnit.SECONDS)
        httpClient.writeTimeout(55, TimeUnit.SECONDS)
        /*
        .connectTimeout(2, TimeUnit.MINUTES)
        .readTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)*/
        // add your other interceptors …
        // add logging as last interceptor

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            // set your desired log level
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
        }


        httpClient.addInterceptor(cacheInterceptor)
        //httpClient.addNetworkInterceptor(provideCacheInterceptor());
        httpClient.cache(cache)
        return httpClient.build()

    }


    fun provideMoshiAdapter(): Moshi = Moshi.Builder().build()


    fun provideRetrofit(okHttpClient: OkHttpClient, apiUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()


    fun provideApiAuth(): ApiPointAuth = retrofit.create(ApiPointAuth::class.java)

    fun provideApiGuard(): ApiPointGuard = retrofit.create(ApiPointGuard::class.java)

}