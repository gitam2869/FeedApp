package com.fan.feedapp.di

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.fan.feedapp.data.local.AppDatabase
import com.fan.feedapp.data.local.dao.TextDao
import com.fan.feedapp.data.local.repository.TextRepositoryImpl
import com.fan.feedapp.data.remote.api.FeedAPI
import com.fan.feedapp.data.remote.repository.VideoListRepositoryImpl
import com.fan.feedapp.data.userstorage.repository.ImageListRepositoryImpl
import com.fan.feedapp.domain.repository.ImageListRepository
import com.fan.feedapp.domain.repository.TextRepository
import com.fan.feedapp.domain.repository.VideoListRepository
import com.fan.feedapp.utils.Keys
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    @Singleton
    fun provideFeedAPI(client: OkHttpClient): FeedAPI {
        return Retrofit
            .Builder()
            .baseUrl(Keys.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(FeedAPI::class.java)
    }

    @Provides
    fun provideHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(interceptor)

        return okHttpClientBuilder.build()
    }

    @Provides
    fun provideVideoListRepository(feedAPI: FeedAPI): VideoListRepository {
        return VideoListRepositoryImpl(feedAPI)
    }

    @Provides
    fun provideImageListRepository(contentResolver: ContentResolver): ImageListRepository {
        return ImageListRepositoryImpl(contentResolver)
    }

    @Provides
    fun provideContentResolver(@ApplicationContext appContext: Context): ContentResolver {
        return appContext.contentResolver
    }

    @Singleton
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "feed_app")
            .build()
    }

    @Provides
    fun provideDao(database: AppDatabase): TextDao {
        return database.textDao
    }

    @Provides
    fun provideTextRepository(textDao: TextDao): TextRepository {
        return TextRepositoryImpl(textDao)
    }

}