package co.edu.udea.compumovil.gr05_20251.lab1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.edu.udea.compumovil.gr05_20251.lab1.ui.theme.Labs20251Gr05Theme

const val CONTACT_TAG = "ContactActivity"
class ContactDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(CONTACT_TAG,"onCreate ContactACtivity")
        enableEdgeToEdge()
        setContent {
            Labs20251Gr05Theme {
            }
        }
    }

}