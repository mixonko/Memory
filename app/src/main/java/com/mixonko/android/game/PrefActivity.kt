package com.mixonko.android.game

import android.os.Bundle
import android.preference.PreferenceActivity
import com.game.memory.card.game.R

class PrefActivity : PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref)
    }
}
