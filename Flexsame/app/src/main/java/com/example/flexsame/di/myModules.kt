package com.example.flexsame.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.flexsame.LoggedInUserViewModel
import com.example.flexsame.network.AuthInterceptor
import com.example.flexsame.ui.login.LoginDataSource
import com.example.flexsame.network.AuthService
import com.example.flexsame.network.KeyService
import com.example.flexsame.repos.*
import com.example.flexsame.ui.home.HomeViewModel
import com.example.flexsame.ui.login.LoginViewModel
import com.example.flexsame.ui.register.RegisterViewModel
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
import okhttp3.OkHttpClient

val myModule : Module = module {

    //gson
    single {
        GsonBuilder()
            .create()
    }


    //custom client with auth interceptor
    single {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor)
            .build()
    }

    //retrofit
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .build()
    }

    //api services
    single {
        provideKeyService(get())
    }

    single {
        provideAuthService(get())
    }

    //database
   // single {
   //     AppDatabase.getInstance(get()).spotDao
   // }

    //connectivity_service
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    //repos
    single {
        KeyRepository(get())
    }
    single {
        LoginRepository(LoginDataSource(get()))
    }
    single {
        RegisterRepository(get())
    }
    single {
        LoggedInUserRepository(get())
    }

    //viewmodels
    viewModel { HomeViewModel() }
    viewModel { TestNFCViewModel()}
    viewModel { WalletViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LoggedInUserViewModel(get()) }

}

private fun provideKeyService(retrofit: Retrofit): KeyService {
    return retrofit.create(KeyService::class.java)
}

private fun provideAuthService(retrofit: Retrofit): AuthService{
    return retrofit.create(AuthService::class.java)
}

