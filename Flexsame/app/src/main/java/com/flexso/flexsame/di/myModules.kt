package com.flexso.flexsame.di

import android.content.Context
import android.net.ConnectivityManager
import co.infinum.goldfinger.rx.RxGoldfinger
import com.flexso.flexsame.LoggedInUserViewModel
import com.flexso.flexsame.network.AuthInterceptor
import com.flexso.flexsame.ui.login.LoginDataSource
import com.flexso.flexsame.network.AuthService
import com.flexso.flexsame.network.KeyService
import com.flexso.flexsame.repos.*
import com.flexso.flexsame.ui.home.HomeViewModel
import com.flexso.flexsame.ui.login.LoginViewModel
import com.flexso.flexsame.ui.register.RegisterViewModel
import com.flexso.flexsame.ui.testNFC.TestNFCViewModel
import com.flexso.flexsame.ui.wallet.WalletViewModel
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.flexso.flexsame.utils.BASE_URL;
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
   //     AppDatabase.getInstance(get()).keyDao
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

