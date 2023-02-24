package fr.melanoxy.roomsystem.data

import android.app.Application
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
//provide context to BluetoothService
    /*@Singleton
    @Provides
    fun provideBluetoothManager(application: Application): BluetoothManager = application.getSystemService(
        Context.BLUETOOTH_SERVICE) as BluetoothManager*/

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext
}