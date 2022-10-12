package uk.shakhzod.gamedrawing.di

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.shakhzod.gamedrawing.data.remote.api.SetupApi
import uk.shakhzod.gamedrawing.repository.DefaultSetupRepository
import uk.shakhzod.gamedrawing.repository.SetupRepository
import uk.shakhzod.gamedrawing.util.connection.Connection
import javax.inject.Singleton

/**
 * Created by Shakhzod Ilkhomov on 12/10/22
 **/

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideSetupRepository(
        setupApi: SetupApi,
        connection: Connection,
        resources: Resources
    ): SetupRepository = DefaultSetupRepository(setupApi, connection, resources)
}