package ru.visionary.mixing.shiny_appearance.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.visionary.mixing.shiny_appearance.data.local.TokenAuthenticator
import ru.visionary.mixing.shiny_appearance.data.local.TokenStorage
import ru.visionary.mixing.shiny_appearance.data.remote.api.AuthService
import ru.visionary.mixing.shiny_appearance.data.remote.api.CommentService
import ru.visionary.mixing.shiny_appearance.data.remote.api.FeedService
import ru.visionary.mixing.shiny_appearance.data.remote.api.FollowService
import ru.visionary.mixing.shiny_appearance.data.remote.api.ProfileImagesService
import ru.visionary.mixing.shiny_appearance.data.remote.api.ImageService
import ru.visionary.mixing.shiny_appearance.data.remote.api.ProcessingService
import ru.visionary.mixing.shiny_appearance.data.remote.api.UserService
import ru.visionary.mixing.shiny_appearance.data.repository.UserRepositoryImpl
import ru.visionary.mixing.shiny_appearance.domain.repository.UserRepository
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
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        tokenStorage: TokenStorage,
        authService: AuthService,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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
    fun provideImageService(retrofit: Retrofit): ImageService {
        return retrofit.create(ImageService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService): UserRepository {
        return UserRepositoryImpl(userService)
    }

    @Provides
    fun provideProfileImagesService(retrofit: Retrofit): ProfileImagesService {
        return retrofit.create(ProfileImagesService::class.java)
    }

    @Provides
    fun provideProcessingService(retrofit: Retrofit): ProcessingService {
        return retrofit.create(ProcessingService::class.java)
    }

    @Provides
    fun provideFeedService(retrofit: Retrofit): FeedService =
        retrofit.create(FeedService::class.java)

    @Provides
    @Singleton
    fun provideCommentService(retrofit: Retrofit): CommentService {
        return retrofit.create(CommentService::class.java)
    }

    @Provides
    fun provideFollowService(retrofit: Retrofit): FollowService {
        return retrofit.create(FollowService::class.java)
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
