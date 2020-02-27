package com.example.flexsame.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.flexsame.network.KeyService
import com.example.flexsame.ui.account.AccountViewModel
import com.example.flexsame.ui.home.HomeViewModel
import com.example.flexsame.ui.testNFC.TestNFCViewModel
import com.example.flexsame.ui.wallet.WalletViewModel
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.flexsame.utils.BASE_URL;

val myModule : Module = module {

    //gson
    single {
        GsonBuilder()
            .create()
    }

    //custom client with auth interceptor
    /*single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor)
            .build()
    }*/

    //retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
           // .client(get())
            .build()
    }

    //api services
    single {
        provideSpotApiService(get())
    }
    //database
   // single {
   //     AppDatabase.getInstance(get()).spotDao
   // }

    //connectivity_service
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    //repos
   // single {
    //    SpotRepository(get(),get(),get(),get())
   // }


    //viewmodels
    viewModel { HomeViewModel() }
    viewModel { TestNFCViewModel()}
    viewModel { AccountViewModel() }
    viewModel { WalletViewModel() }

}

private fun provideSpotApiService(retrofit: Retrofit): KeyService {
    return retrofit.create(KeyService::class.java)
}

