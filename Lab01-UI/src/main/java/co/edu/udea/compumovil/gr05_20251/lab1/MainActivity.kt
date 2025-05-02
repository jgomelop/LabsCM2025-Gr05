package co.edu.udea.compumovil.gr05_20251.lab1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import co.edu.udea.compumovil.gr05_20251.lab1.ui.contact.ContactFormViewModel
import co.edu.udea.compumovil.gr05_20251.lab1.ui.personal.PersonalFormScreen
import co.edu.udea.compumovil.gr05_20251.lab1.ui.personal.PersonalFormViewModel
import androidx.navigation.compose.rememberNavController
import co.edu.udea.compumovil.gr05_20251.lab1.ui.contact.ContactFormScreen
import androidx.compose.runtime.getValue
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
//                    ContactFormScreen()
                    AppNavHost()
                }
            }
        }
    }
}

sealed class Screen(val route: String) {
    object PersonalForm : Screen("personal_form")
    object ContactForm : Screen("contact_form")
}

@Composable
fun AppNavHost(
    personalFormViewModel: PersonalFormViewModel = viewModel(),
    contactFormViewModel: ContactFormViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.PersonalForm.route) {
        composable(Screen.PersonalForm.route) {
            PersonalFormScreen(
                viewModel = personalFormViewModel,
                onNext = {
                    navController.navigate(Screen.ContactForm.route)
                }
            )
        }

        composable(Screen.ContactForm.route) {
            val personalFormUiState by personalFormViewModel.uiState.collectAsState()

            ContactFormScreen(
                viewModel = contactFormViewModel,
                personalFormUiState = personalFormUiState
            )
        }
    }

}