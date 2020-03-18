package com.sinch.smsverification.config

import com.sinch.smsverification.SmsVerificationService
import com.sinch.verificationcore.config.general.GeneralConfig
import com.sinch.verificationcore.config.method.VerificationMethodConfig

class SmsVerificationConfig(
    config: GeneralConfig,
    number: String,
    custom: String = ""
) : VerificationMethodConfig<SmsVerificationService>(
    config, number, custom, apiService = config.retrofit.create(SmsVerificationService::class.java)
)