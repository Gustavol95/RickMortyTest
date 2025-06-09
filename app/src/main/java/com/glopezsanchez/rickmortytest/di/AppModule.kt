package com.glopezsanchez.rickmortytest.di

import com.glopezsanchez.rickmortytest.data.remote.ApiService
import com.glopezsanchez.rickmortytest.data.repository.CharacterRepositoryImpl
import com.glopezsanchez.rickmortytest.domain.repository.CharacterRepository
import com.glopezsanchez.rickmortytest.ui.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object AppModule {

    private fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .build()
    }


    private fun provideConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()


    private fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/")
            .client(okHttpClient)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    private fun provideService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    private val networkModule = module {
        single { provideHttpClient() }
        single { provideConverterFactory() }
        single { provideRetrofit(get(), get()) }
        single { provideService(get()) }
    }

    private val repositoryModule = module {
        single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    }

    private val viewModelModule = module {
        viewModel { MainViewModel(get()) }
    }

    val appModule = listOf(networkModule, repositoryModule, viewModelModule)
}
