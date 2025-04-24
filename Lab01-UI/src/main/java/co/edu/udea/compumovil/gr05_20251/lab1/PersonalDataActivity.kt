package co.edu.udea.compumovil.gr05_20251.lab1

import android.content.Intent
import android.os.Bundle
import android.util.Log

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import co.edu.udea.compumovil.gr05_20251.lab1.ui.theme.Labs20251Gr05Theme

const val PERSONAL_DATA_TAG = "PersonalActivity"
class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, ContactDataActivity::class.java)
        Log.d(PERSONAL_DATA_TAG,"onCreate PersonalActivity")
        enableEdgeToEdge()

        setContent {
            Labs20251Gr05Theme {
                Column {
                    PersonalDataForm()
                    Button(onClick = {
                        startActivity(intent)
                    }) {
                        Text("Siguiente")
                    }
                }

            }
        }
    }
}

@Composable
fun PersonalDataForm() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        FormTitle()
        HorizontalDivider(thickness = 2.dp)
    }
}

@Composable
fun FormTitle() {
    Text(
        text = "Información Personal",
        modifier = Modifier.padding(vertical = 16.dp)
    )
}



