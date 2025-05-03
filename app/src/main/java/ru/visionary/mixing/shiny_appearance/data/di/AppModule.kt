package ru.visionary.mixing.shiny_appearance.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.visionary.mixing.shiny_appearance.data.local.TokenAuthenticator
import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.remote.api.AuthService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthService(): AuthService {
        return Retrofit.Builder()
            .baseUrl("http://185.188.182.20:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenStorage: TokenStorage,
        authService: AuthService
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(TokenAuthenticator(tokenStorage, authService))
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                runBlocking {
                    tokenStorage.getAccessToken()?.let {
                        requestBuilder.addHeader("Authorization", "Bearer $it")
                    }
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideProtectedApiRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://185.188.182.20:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}
