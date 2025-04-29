package co.edu.udea.compumovil.gr05_20251.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import co.edu.udea.compumovil.gr05_20251.lab1.ui.theme.Labs20251Gr05Theme


class PersonalDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labs20251Gr05Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PersonalDataScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }
}


