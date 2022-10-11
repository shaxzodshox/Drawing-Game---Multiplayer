package uk.shakhzod.gamedrawing.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.shakhzod.gamedrawing.data.remote.api.SetupApi
import uk.shakhzod.gamedrawing.util.Constants.HTTP_BASE_URL
import uk.shakhzod.gamedrawing.util.Constants.HTTP_BASE_URL_LOCALHOST
import uk.shakhzod.gamedrawing.util.Constants.USE_LOCALHOST
import uk.shakhzod.gamedrawing.util.DispatcherProvider
import javax.inject.Singleton

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideSetupApi(okHttpClient: OkHttpClient): SetupApi {
        return Retrofit.Builder().baseUrl(
            if (USE_LOCALHOST) HTTP_BASE_URL_LOCALHOST else HTTP_BASE_URL
        )
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(SetupApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGsonInstance(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return object : DispatcherProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
        }
    }
}