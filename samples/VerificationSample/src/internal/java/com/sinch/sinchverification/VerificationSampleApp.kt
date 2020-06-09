package com.sinch.sinchverification

import android.app.Application
import android.view.MenuItem
import com.sinch.logging.Log
import com.sinch.logging.LogcatAppender
import com.sinch.verificationcore.auth.AppKeyAuthorizationMethod
import com.sinch.verificationcore.config.general.GlobalConfig
import com.sinch.verificationcore.config.general.SinchGlobalConfig
import okhttp3.logging.HttpLoggingInterceptor


class VerificationSampleApp : Application() {

    var globalConfig: GlobalConfig = buildGlobalConfig(
        apiHost = BuildConfig.API_BASE_URL_PROD,
        appKey = BuildConfig.APP_KEY
    )
        private set

    override fun onCreate() {
        super.onCreate()
        initFlipper()
        initLogger()
    }

    private fun initFlipper() {
        FlipperInitializer.initFlipperPlugins(this)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Log.init(LogcatAppender())
        }
    }

    private fun buildGlobalConfig(apiHost: String, appKey: String): GlobalConfig =
        SinchGlobalConfig.Builder.instance.applicationContext(this)
            .authorizationMethod(AppKeyAuthorizationMethod(appKey))
            .apiHost(apiHost)
            .interceptors(FlipperInitializer.okHttpFlipperInterceptors + HttpLoggingInterceptor().apply {
                setLevel(
                    HttpLoggingInterceptor.Level.BODY
                )
            })
            .build()

    fun onDevelopmentOptionSelected(item: MenuItem): Boolean {
        item.isChecked = true
        globalConfig = when (item.itemId) {
            R.id.productionApi -> buildGlobalConfig(
                BuildConfig.API_BASE_URL_PROD,
                BuildConfig.APP_KEY_PROD
            )
            R.id.ftest1Api -> buildGlobalConfig(
                BuildConfig.API_BASE_URL_FTEST1,
                BuildConfig.APP_KEY_FTEST1
            )
            R.id.ftest2Api -> buildGlobalConfig(
                BuildConfig.API_BASE_URL_FTEST2,
                BuildConfig.APP_KEY_FTEST2
            )
            else -> throw RuntimeException("Menu item with ${item.itemId} not handled")
        }
        return true
    }

}