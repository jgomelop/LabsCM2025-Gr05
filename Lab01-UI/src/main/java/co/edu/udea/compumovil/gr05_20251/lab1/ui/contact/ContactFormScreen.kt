package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact
import co.edu.udea.compumovil.gr05_20251.lab1.R

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ContactFormScreen(viewModel: ContactFormViewModel = viewModel()) {
    val estado = viewModel.uiState
    val paises = stringArrayResource(id = R.array.paises_latinoamerica)
    val ciudades = stringArrayResource(id = R.array.ciudades_colombia)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = estado.telefono,
            onValueChange = viewModel::onTelefonoChanged,
            label = { Text("Teléfono*") },
            isError = estado.errores.containsKey("telefono"),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        estado.errores["telefono"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        OutlinedTextField(
            value = estado.direccion,
            onValueChange = viewModel::onDireccionChanged,
            label = { Text("Dirección") },
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text("Email*") },
            isError = estado.errores.containsKey("email"),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        estado.errores["email"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        DropdownSelector(
            label = "País*",
            opciones = paises.toList(),
            seleccion = estado.pais,
            onSeleccion = viewModel::onPaisChanged,
            error = estado.errores["pais"]
        )

        // Solo mostrar el Dropdown de Ciudad si el país seleccionado es "Colombia"
        if (estado.pais == "Colombia") {
            DropdownSelector(
                label = "Ciudad",
                opciones = ciudades.toList(),
                seleccion = estado.ciudad,
                onSeleccion = viewModel::onCiudadChanged
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    // Aquí podrías enviar los datos o mostrar un mensaje de éxito
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    opciones: List<String>,
    seleccion: String,
    onSeleccion: (String) -> Unit,
    error: String? = null
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expandido,
            onExpandedChange = { expandido = !expandido }
        ) {
            OutlinedTextField(
                value = seleccion,
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                isError = error != null,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            onSeleccion(opcion)
                            expandido = false
                        }
                    )
                }
            }
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
