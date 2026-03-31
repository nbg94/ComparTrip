package com.bedoya.compartrip

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp  // esta anotación activa Hilt en toda la app
class CompartripApp : Application()