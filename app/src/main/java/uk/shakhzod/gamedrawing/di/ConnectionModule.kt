package uk.shakhzod.gamedrawing.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.shakhzod.gamedrawing.util.connection.Connection
import javax.inject.Singleton

/**
 * Created by Shakhzod Ilkhomov on 11/10/22
 **/

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {
    @Singleton
    @Provides
    fun provideConnection(@ApplicationContext context: Context) : Connection = Connection(context)
}