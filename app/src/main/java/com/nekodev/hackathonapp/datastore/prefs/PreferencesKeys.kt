package com.nekodev.hackathonapp.datastore.prefs

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val JWT_TOKEN = stringPreferencesKey("jwt_token")
}