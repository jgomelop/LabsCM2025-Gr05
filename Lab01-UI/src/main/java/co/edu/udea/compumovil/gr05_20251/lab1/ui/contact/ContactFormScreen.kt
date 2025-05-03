package co.edu.udea.compumovil.gr05_20251.lab1.ui.contact
import android.util.Log
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
import co.edu.udea.compumovil.gr05_20251.lab1.ui.personal.PersonalFormUiState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import co.edu.udea.compumovil.gr05_20251.lab1.Logger.imprimirInformacionForms
import co.edu.udea.compumovil.gr05_20251.lab1.ui.utils.isLandscape
import androidx.compose.ui.text.style.TextAlign

const val ContactFormScreenLogTag = "ConstactScreen"

@Composable
fun ContactFormScreen(
    viewModel: ContactFormViewModel,
    personalFormUiState: PersonalFormUiState
){

    if (isLandscape()) {
        Log.d(ContactFormScreenLogTag,"ContactScreenLandscape")
        ContactFormScreenLandscape(
            viewModel,
            personalFormUiState
        )
    } else {
        Log.d(ContactFormScreenLogTag,"ContactScreenPortrait")
        ContactFormScreenPortrait (
            viewModel,
            personalFormUiState
        )
    }
}


@Composable
fun ContactFormScreenPortrait(
    viewModel: ContactFormViewModel,
    personalFormUiState: PersonalFormUiState
) {
    val contactFormUiState = viewModel.uiState
    val paises = stringArrayResource(id = R.array.paises_latinoamerica)
    val ciudades = stringArrayResource(id = R.array.ciudades_colombia)
    
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_contacto),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            value = contactFormUiState.telefono,
            onValueChange = viewModel::onTelefonoChanged,
            label = { Text(stringResource(R.string.telefono_label)) },
            isError = contactFormUiState.errores.containsKey("telefono"),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )
        contactFormUiState.errores["telefono"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        OutlinedTextField(
            value = contactFormUiState.direccion,
            onValueChange = viewModel::onDireccionChanged,
            label = { Text(stringResource(R.string.direccion_label)) },
            keyboardOptions = KeyboardOptions(autoCorrectEnabled = false),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = contactFormUiState.email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text(stringResource(R.string.email_tag)) },
            isError = contactFormUiState.errores.containsKey("email"),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        contactFormUiState.errores["email"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        DropdownSelector(
            label = stringResource(R.string.pais_label),
            opciones = paises.toList(),
            seleccion = contactFormUiState.pais,
            onSeleccion = viewModel::onPaisChanged,
            error = contactFormUiState.errores["pais"]
        )

        // Solo mostrar el Dropdown de Ciudad si el país seleccionado es "Colombia"
        if (contactFormUiState.pais == "Colombia") {
            DropdownSelector(
                label = stringResource(R.string.ciudad_label),
                opciones = ciudades.toList(),
                seleccion = contactFormUiState.ciudad,
                onSeleccion = viewModel::onCiudadChanged
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    imprimirInformacionForms(personalFormUiState, contactFormUiState)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_finalizar))
        }
    }
}

@Composable
fun ContactFormScreenLandscape(
    viewModel: ContactFormViewModel = viewModel(),
    personalFormUiState: PersonalFormUiState
) {
    val contactFormUiState = viewModel.uiState
    val paises = stringArrayResource(id = R.array.paises_latinoamerica)
    val ciudades = stringArrayResource(id = R.array.ciudades_colombia)

    Column {
        Text(
            text = stringResource(R.string.titulo_contacto),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Columna izquierda: Teléfono, Dirección, Email
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = contactFormUiState.telefono,
                    onValueChange = viewModel::onTelefonoChanged,
                    label = { Text(stringResource(R.string.telefono_label)) },
                    isError = contactFormUiState.errores.containsKey("telefono"),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                contactFormUiState.errores["telefono"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

                OutlinedTextField(
                    value = contactFormUiState.direccion,
                    onValueChange = viewModel::onDireccionChanged,
                    label = { Text(stringResource(R.string.direccion_label)) },
                    keyboardOptions = KeyboardOptions(autoCorrectEnabled = false),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = contactFormUiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text(stringResource(R.string.email_tag)) },
                    isError = contactFormUiState.errores.containsKey("email"),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                contactFormUiState.errores["email"]?.let {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }

            // Columna derecha: País, Ciudad (si aplica), Botón
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DropdownSelector(
                    label = stringResource(R.string.pais_label),
                    opciones = paises.toList(),
                    seleccion = contactFormUiState.pais,
                    onSeleccion = viewModel::onPaisChanged,
                    error = contactFormUiState.errores["pais"]
                )

                if (contactFormUiState.pais == "Colombia") {
                    DropdownSelector(
                        label = stringResource(R.string.ciudad_label),
                        opciones = ciudades.toList(),
                        seleccion = contactFormUiState.ciudad,
                        onSeleccion = viewModel::onCiudadChanged
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        if (viewModel.validarFormulario()) {
                            imprimirInformacionForms(personalFormUiState, contactFormUiState)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.btn_finalizar))
                }
            }
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
