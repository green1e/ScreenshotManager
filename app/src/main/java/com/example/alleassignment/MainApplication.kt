package com.example.alleassignment

import android.app.Application
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.pixplicity.easyprefs.library.Prefs

class MainApplication : Application() {

    companion object {
        private var recognizer: TextRecognizer? = null
        fun getRecognizer(): TextRecognizer {
            if (recognizer == null) {
                recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            }
            return recognizer!!
        }

        private var labeler: ImageLabeler? = null
        fun getLabeler(): ImageLabeler {
            if (labeler == null) {
                labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            }
            return labeler!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        Prefs.Builder()
            .setContext(this)
            .setMode(MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}