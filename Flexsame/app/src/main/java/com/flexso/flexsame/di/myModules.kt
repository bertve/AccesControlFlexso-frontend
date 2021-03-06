package com.flexso.flexsame.di

import android.content.Context
import android.net.ConnectivityManager
import com.flexso.flexsame.LoggedInUserViewModel
import com.flexso.flexsame.network.*
import com.flexso.flexsame.repos.*
import com.flexso.flexsame.ui.admin.AdminViewModel
import com.flexso.flexsame.ui.company.CompanyViewModel
import com.flexso.flexsame.ui.home.HomeViewModel
import com.flexso.flexsame.ui.login.LoginDataSource
import com.flexso.flexsame.ui.login.LoginViewModel
import com.flexso.flexsame.ui.office.OfficeViewModel
import com.flexso.flexsame.ui.register.RegisterViewModel
import com.flexso.flexsame.ui.settings.SettingsViewModel
import com.flexso.flexsame.ui.wallet.WalletViewModel
import com.flexso.flexsame.utils.BASE_URL
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val myModule: Module = module {

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

    single {
        provideAdminService(get())
    }

    single {
        provideCompanyService(get())
    }

    //connectivity_service
    single { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    //repos
    single {
        KeyRepository(get(), get(), get())
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
    single {
        AdminRepository(get())
    }
    single {
        CompanyRepository(get())
    }
    single {
        OfficeRepository(get())
    }

    //viewmodels
    viewModel { HomeViewModel(get()) }
    viewModel { WalletViewModel(get()) }
    viewModel { LoginViewModel(get(),get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { LoggedInUserViewModel(get(),get()) }
    viewModel { SettingsViewModel() }
    viewModel { AdminViewModel(get()) }
    viewModel { CompanyViewModel(get()) }
    viewModel { OfficeViewModel(get()) }
}

private fun provideKeyService(retrofit: Retrofit): KeyService {
    return retrofit.create(KeyService::class.java)
}

private fun provideAuthService(retrofit: Retrofit): AuthService {
    return retrofit.create(AuthService::class.java)
}

private fun provideAdminService(retrofit: Retrofit): AdminService {
    return retrofit.create(AdminService::class.java)
}

private fun provideCompanyService(retrofit: Retrofit): CompanyService {
    return retrofit.create(CompanyService::class.java)
}


