package com.example.jetpackpref

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import java.io.File

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)
    }


    class SettingsFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.pref_screen, rootKey)
            val cbPref: CheckBoxPreference? = findPreference("key_CheckBoxPreference")
            val sPref: SwitchPreference? = findPreference("key_SwitchPreference")
            cbPref!!.onPreferenceChangeListener = this
            sPref!!.onPreferenceChangeListener = this
        }

        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            Toast.makeText(context, "${preference!!.title} $newValue", Toast.LENGTH_SHORT).show()
            return true
        }

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        Toast.makeText(this, "value: ${sharedPreferences!!.getBoolean(key, false)} pref: $key", Toast.LENGTH_SHORT).show()

        val file = File(filesDir, "arquivo.txt")
        file.createNewFile()

        writeContent(file, readContent(file) +
                "Pref.: $key, Valor: ${sharedPreferences!!.all[key]}\n")
    }

    private fun readContent(file: File): String {
        file.inputStream().use {
            return it.readBytes().decodeToString()
        }
    }

    private fun writeContent(file: File, write: String){
        file.outputStream().use {
            it.write(write.toByteArray())
        }
    }
}