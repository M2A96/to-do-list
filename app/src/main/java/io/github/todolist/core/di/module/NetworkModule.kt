package io.github.todolist.core.di.module

import com.facebook.stetho.okhttp3.BuildConfig
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.todolist.core.di.qualifier.WithToken
import io.github.todolist.core.di.qualifier.WithoutToken
import io.github.todolist.data.api.MockTaskApiService
import io.github.todolist.data.api.TasksApi
import io.github.todolist.util.SecretFields
import okhttp3.Authenticator
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import javax.inject.Singleton

/**
 * The main [Module] for providing network-related classes
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * provides Gson with custom [Date] converter for [Long] epoch times
     */
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            // Deserializer to convert json long value into Date
            .registerTypeAdapter(
                Date::class.java,
                JsonDeserializer { json, _, _ ->
                    Date(json.asJsonPrimitive.asLong)
                }
            )
            // Serializer to convert Date value into long json primitive
            .registerTypeAdapter(
                Date::class.java,
                JsonSerializer<Date> { src, _, _ ->
                    JsonPrimitive(src.time)
                }
            )
            .create()
    }

    /**
     * provides shared [Headers] to be added into [OkHttpClient] instances
     */
    @Singleton
    @Provides
    fun provideSharedHeaders(): Headers {
        return Headers.Builder()
            .add("Accept", "*/*")
            .add("User-Agent", "mobile")
            .build()
    }

    /**
     * Provides [OkHttpClient] instance for token based api services
     *
     * @param preferencesHelper to access saved token, provided by [AppModule.provideAppPreferencesHelper]
     * @param headers default shared headers to be added in http request, provided by [provideSharedHeaders]
     * @param authenticator instance of [TokenAuthenticator] for handling UNAUTHORIZED errors,
     * provided by [provideAuthenticator]
     *
     * @return an instance of [OkHttpClient]
     */
    @Singleton
    @Provides
    @WithToken
    fun provideOkHttpClientWithToken(
        headers: Headers,
        authenticator: Authenticator,
        secretFields: SecretFields
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // if the app is in DEBUG mode OkHttp will show complete log in logcat and Stetho framework
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            // Stetho will be initialized here
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        builder.interceptors().add(
            Interceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                    // add default shared headers to every http request
                    .headers(headers)
                    // add tokenType and token to Authorization header of request
//                    .addHeader(
//                        "Authorization",
//                        preferencesHelper.tokenType + " " + preferencesHelper.token
//                    )
                    .method(request.method, request.body)
                chain.proceed(requestBuilder.build())
            }
        )

        builder.authenticator(authenticator)

        return builder.build()
    }

    /**
     * provides instance of [OkHttpClient] for without-token api services
     *
     * @param headers default shared headers provided by [provideSharedHeaders]
     * @return an instance of [OkHttpClient]
     */
    @Singleton
    @Provides
    @WithoutToken
    fun provideOkHttpClient(headers: Headers, secretFields: SecretFields): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)

            builder.addNetworkInterceptor(StethoInterceptor())
        }

        builder.interceptors().add(
            Interceptor { chain ->
                val request = chain.request()
                val requestBuilder = request.newBuilder()
                    .headers(headers)
                    .method(request.method, request.body)
                chain.proceed(requestBuilder.build())
            }
        )

        return builder.build()
    }

    /**
     * provide an instance of [Retrofit] for without-token api services
     *
     * @param okHttpClient an instance of without-token [okHttpClient] provided by [provideOkHttpClient]
     * @param gson an instance of gson provided by [provideGson] to use as retrofit converter factory
     *
     * @return an instance of [Retrofit] for without-token api calls
     */
    @Singleton
    @Provides
    @WithoutToken
    fun provideRetrofit(
        @WithoutToken okHttpClient: OkHttpClient,
        gson: Gson,
        secretFields: SecretFields
    ): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            // create gson converter factory
            .addConverterFactory(GsonConverterFactory.create(gson))
            // get base url from SecretFields interface
            .baseUrl(secretFields.getBaseUrl())
            .build()
    }

    /**
     * provide an instance of [Retrofit] for with-token api services
     *
     * @param okHttpClient an instance of with-token [okHttpClient] provided by [provideOkHttpClientWithToken]
     * @param gson an instance of gson provided by [provideGson] to use as retrofit converter factory
     *
     * @return an instance of [Retrofit] for with-token api calls
     */
    @Singleton
    @Provides
    @WithToken
    fun provideRetrofitWithToken(@WithToken okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder().client(okHttpClient)
            // create gson converter factory
            .addConverterFactory(GsonConverterFactory.create(gson))
            // get base url from SecretFields interface
            .baseUrl(SecretFields().getBaseUrl())
            .build()
    }

    @Provides
    @Singleton
    fun provideTasksApi(@WithoutToken retrofit: Retrofit): TasksApi {
        // Create and pre-populate the mock service
        return MockTaskApiService().apply {
            prepopulateWithSampleData()
        }
    }
}