package com.appxs.apex.data.config

import com.appxs.apex.BuildConfig

class AIConfigImpl: AIConfig {
    override val token: String = BuildConfig.AI_TOKEN
    override val account: String = BuildConfig.AI_ACCOUNT
}