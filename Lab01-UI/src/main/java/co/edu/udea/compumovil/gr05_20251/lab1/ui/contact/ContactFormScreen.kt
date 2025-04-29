package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact
import co.edu.udea.compumovil.gr05_20251.lab1.R

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactFormScreen(
    viewModel: ContactFormViewModel = viewModel(),
    onFormSubmitted: () -> Unit = {}
) {
    val paisesList = stringArrayResource(id = R.array.paises_latinoamerica)
    val ciudadesColombia = stringArrayResource(id = R.array.ciudades_colombia)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Formulario de Contacto",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campo de Teléfono (obligatorio)
        OutlinedTextField(
            value = viewModel.uiState.telefono,
            onValueChange = { viewModel.actualizarTelefono(it) },
            label = { Text("Teléfono *") },
            isError = viewModel.uiState.errorTelefono != null,
            supportingText = {
                viewModel.uiState.errorTelefono?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Campo de Dirección
        OutlinedTextField(
            value = viewModel.uiState.direccion,
            onValueChange = { viewModel.actualizarDireccion(it) },
            label = { Text("Dirección") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Campo de Email (obligatorio)
        OutlinedTextField(
            value = viewModel.uiState.email,
            onValueChange = { viewModel.actualizarEmail(it) },
            label = { Text("Email *") },
            isError = viewModel.uiState.errorEmail != null,
            supportingText = {
                viewModel.uiState.errorEmail?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Campo de País (obligatorio con dropdown)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            // Utilizamos ExposedDropdownMenuBox para el país
            ExposedDropdownMenuBox(
                expanded = viewModel.expandidoPaises,
                onExpandedChange = { viewModel.alternarMenuPaises() }
            ) {
                OutlinedTextField(
                    value = viewModel.uiState.paisSeleccionado,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("País *") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.expandidoPaises)
                    },
                    isError = viewModel.uiState.errorPais != null,
                    supportingText = {
                        viewModel.uiState.errorPais?.let {
                            Text(it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = viewModel.expandidoPaises,
                    onDismissRequest = { viewModel.alternarMenuPaises() }
                ) {
                    paisesList.forEach { pais ->
                        DropdownMenuItem(
                            text = { Text(pais) },
                            onClick = {
                                viewModel.actualizarPais(pais)
                            }
                        )
                    }
                }
            }
        }

        // Campo de Ciudad (con dropdown)
        if (viewModel.uiState.paisSeleccionado == "Colombia") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = viewModel.expandidoCiudades,
                    onExpandedChange = { viewModel.alternarMenuCiudades() }
                ) {
                    OutlinedTextField(
                        value = viewModel.uiState.ciudadSeleccionada,
                        onValueChange = { },
                        label = { Text("Ciudad") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.expandidoCiudades)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = viewModel.expandidoCiudades,
                        onDismissRequest = { viewModel.alternarMenuCiudades() }
                    ) {
                        ciudadesColombia.forEach { ciudad ->
                            DropdownMenuItem(
                                text = { Text(ciudad) },
                                onClick = {
                                    viewModel.actualizarCiudad(ciudad)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Botón para enviar el formulario
        Button(
            onClick = {
                if (viewModel.enviarFormulario()) {
                    onFormSubmitted()
                }
            },
            enabled = viewModel.uiState.esFormularioValido,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Enviar")
        }
    }
}