package com.sinch.verificationcore.initiation

import com.sinch.verificationcore.initiation.response.InitiationListener
import com.sinch.verificationcore.initiation.response.InitiationResponseData
import com.sinch.verificationcore.internal.VerificationStateListener
import com.sinch.verificationcore.internal.VerificationStateStatus
import com.sinch.verificationcore.internal.error.VerificationState
import com.sinch.verificationcore.internal.utils.ApiCallback
import retrofit2.Response

open class InitiationApiCallback<T : InitiationResponseData>(
    private val listener: InitiationListener<T>,
    private val statusListener: VerificationStateListener
) :
    ApiCallback<T> {

    override fun onSuccess(data: T, response: Response<T>) {
        statusListener.update(VerificationState.Initialization(VerificationStateStatus.SUCCESS))
        listener.onInitiated(data, response.headers()["Content-Language"] ?: "")
    }

    override fun onError(t: Throwable) {
        statusListener.update(VerificationState.Initialization(VerificationStateStatus.ERROR))
        listener.onInitializationFailed(t)
    }

}