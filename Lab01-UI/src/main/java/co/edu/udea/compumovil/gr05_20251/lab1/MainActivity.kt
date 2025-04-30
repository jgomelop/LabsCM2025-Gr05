package co.edu.udea.compumovil.gr05_20251.lab1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.udea.compumovil.gr05_20251.lab1.ui.contact.ContactFormScreen
import co.edu.udea.compumovil.gr05_20251.lab1.ui.personal.PersonalFormScreen
import co.edu.udea.compumovil.gr05_20251.lab1.ui.personal.PersonalFormViewModel

import co.edu.udea.compumovil.gr05_20251.lab1.ui.theme.Labs20251Gr05Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Labs20251Gr05Theme {
                // Un contenedor Surface con el color de fondo del tema
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Aquí es donde llamamos al composable PersonalForm
//                    val viewModel: PersonalFormViewModel = viewModel()
                    PersonalFormScreen()
//                    ContactFormScreen()
                }
            }
        }
    }
}