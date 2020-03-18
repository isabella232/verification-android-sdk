package com.sinch.verificationcore.initiation

import com.sinch.verificationcore.internal.VerificationMethodType

interface VerificationInitiationData {
    val method: VerificationMethodType
    val identity: VerificationIdentity
    val honourEarlyReject: Boolean
    val custom: String?
}