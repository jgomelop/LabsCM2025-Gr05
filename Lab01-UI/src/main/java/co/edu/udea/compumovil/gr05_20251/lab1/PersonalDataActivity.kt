package co.edu.udea.compumovil.gr05_20251.lab1

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import co.edu.udea.compumovil.gr05_20251.lab1.ui.theme.Labs20251Gr05Theme

const val PERSONAL_DATA_TAG = "PersonalActivity"
class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(PERSONAL_DATA_TAG,"onCreate PersonalActivity")
        enableEdgeToEdge()
        setContent {
            Labs20251Gr05Theme {
                Button(onClick = {
                    val intent = Intent(this, ContactDataActivity::class.java)
                    startActivity(intent)
                }) {
                    Text("Siguiente")
                }
            }
        }
    }
}



