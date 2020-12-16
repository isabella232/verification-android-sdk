package com.sinch.verification.all.auto

import android.app.Application
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.sinch.verification.all.auto.config.AutoVerificationConfig
import com.sinch.verification.all.auto.initialization.AutoInitializationListener
import com.sinch.verification.core.config.general.GlobalConfig
import com.sinch.verification.core.verification.response.VerificationListener
import com.sinch.verification.utils.permission.Permission
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(
    RobolectricTestRunner::class
)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class AutoVerificationMethodTests {

    private val appContext = ApplicationProvider.getApplicationContext<Application>()

    private val mockedService = mockk<AutoVerificationService>(relaxed = true)

    private val mockedGlobalConfig = spyk<GlobalConfig> {
        every { context } returns (appContext)
        every { retrofit } returns mockk {
            every { create(AutoVerificationService::class.java) } returns mockedService
        }
    }

    @MockK
    lateinit var mockedInitListener: AutoInitializationListener

    @MockK
    lateinit var mockedVerificationListener: VerificationListener

    @Before
    fun setupUp() {
        MockKAnnotations.init(this, relaxed = true)
        Shadows.shadowOf(appContext).grantPermissions(Permission.READ_CALL_LOG.androidValue)
    }


    private fun prepareVerification() =
        AutoVerificationMethod.Builder.instance
            .config(
                AutoVerificationConfig.Builder.instance
                    .globalConfig(mockedGlobalConfig)
                    .number(Constants.phone)
                    .build()
            )
            .initializationListener(mockedInitListener)
            .verificationListener(mockedVerificationListener)
            .build()

}