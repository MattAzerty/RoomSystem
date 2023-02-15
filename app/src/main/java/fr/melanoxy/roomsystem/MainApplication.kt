package fr.melanoxy.roomsystem

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/*
Hilt's code generation, including a base class for your application
that serves as the application-level dependency container.
 */

@HiltAndroidApp
class MainApplication : Application()