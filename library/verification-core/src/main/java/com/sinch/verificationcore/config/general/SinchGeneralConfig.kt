package com.sinch.verificationcore.config.general

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sinch.verificationcore.auth.AuthorizationInterceptor
import com.sinch.verificationcore.auth.AuthorizationMethod
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class SinchGeneralConfig private constructor(
    override val context: Context,
    override val retrofit: Retrofit
) : GeneralConfig {

    class Builder : ConfigBuilder {

        private lateinit var context: Context
        private lateinit var apiHost: String
        private lateinit var authorizationMethod: AuthorizationMethod
        private var additionalInterceptors: List<Interceptor> = emptyList()

        private val baseUrl: String get() = "${apiHost}verification/v1/"

        override fun build(): GeneralConfig {
            val okHttpClient =
                OkHttpClient().newBuilder()
                    .addInterceptor(
                        AuthorizationInterceptor(
                            authorizationMethod
                        )
                    )
                    .apply { additionalInterceptors.forEach { addInterceptor(it) } }
                    .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(
                    Json(JsonConfiguration.Stable)
                        .asConverterFactory("application/json".toMediaType())
                )
                .client(okHttpClient)
                .build()
            return SinchGeneralConfig(
                context,
                retrofit
            )
        }

        override fun context(context: Context): ConfigBuilder = apply { this.context = context }

        override fun authMethod(authorizationMethod: AuthorizationMethod): ConfigBuilder =
            apply { this.authorizationMethod = authorizationMethod }

        override fun apiHost(apiHost: String): ConfigBuilder = apply { this.apiHost = apiHost }

        override fun interceptors(interceptors: List<Interceptor>): ConfigBuilder =
            apply { this.additionalInterceptors = interceptors }

    }

}