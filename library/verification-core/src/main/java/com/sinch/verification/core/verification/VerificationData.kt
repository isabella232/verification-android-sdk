package com.sinch.verification.core.verification

import com.sinch.verification.core.internal.VerificationMethodType

/**
 * Common fields of verification data that is passed to the backend with the verification request.
 */
interface VerificationData {

    /**
     * Method of the verification.
     */
    val method: VerificationMethodType

    /**
     * Source of the verification code.
     */
    val source: VerificationSourceType
}