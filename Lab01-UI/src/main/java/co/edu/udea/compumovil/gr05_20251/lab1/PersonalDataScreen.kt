package co.edu.udea.compumovil.gr05_20251.lab1

import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color


@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun PersonalDataScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val isLandscape = isLandscape()

    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var selectedGender by rememberSaveable { mutableStateOf("Masculino") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    val educationLevels =
        listOf("Primaria", "Secundaria", "Técnico", "Tecnólogo", "Universitario", "Posgrado")
    var selectedEducation by rememberSaveable { mutableStateOf("Seleccionar escolaridad") }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val dropdownIcon = Icons.Filled.ArrowDropDown

    val scrollState = rememberScrollState()

    if (isLandscape) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Nombres*") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = false,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Next) }
                    )
                )
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Apellido*") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = false,
                        imeAction = ImeAction.Next
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Sexo")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("Masculino", "Femenino", "Otro").forEach { gender ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = selectedGender == gender,
                            onClick = { selectedGender = gender }
                        )
                        Text(text = gender)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = birthDate,
                    onValueChange = {},
                    label = { Text("Fecha de nacimiento*") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .clickable {
                            showDatePickerDialog(context) { selectedDate ->
                                birthDate = selectedDate
                            }
                        },
                    enabled = false
                )

                Button(onClick = {
                    showDatePickerDialog(context) { selectedDate ->
                        birthDate = selectedDate
                    }
                }) {
                    Text("Cambiar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = selectedEducation,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Grado de escolaridad") },
                        trailingIcon = {
                            Icon(
                                imageVector = dropdownIcon,
                                contentDescription = null,
                                modifier = Modifier.clickable { isDropdownExpanded = true }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDropdownExpanded = true }
                    )

                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        educationLevels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    selectedEducation = level
                                    isDropdownExpanded = false
                                    Toast.makeText(
                                        context,
                                        "Seleccionaste: $level",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (firstName.isBlank() || lastName.isBlank() || birthDate.isBlank()) {
                            Toast.makeText(
                                context,
                                "Por favor completa los campos obligatorios",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val intent = Intent(context, ContactDataActivity::class.java)
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text("Siguiente")
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombres*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )

            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(12.dp))

            Text("Sexo")
            Row {
                listOf("Masculino", "Femenino", "Otro").forEach { gender ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        RadioButton(
                            selected = selectedGender == gender,
                            onClick = { selectedGender = gender }
                        )
                        Text(text = gender)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = birthDate,
                onValueChange = {},
                label = { Text("Fecha de nacimiento*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePickerDialog(context) { selectedDate ->
                            birthDate = selectedDate
                        }
                    },
                enabled = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedEducation,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Escolaridad") },
                    trailingIcon = {
                        Icon(
                            imageVector = dropdownIcon,
                            contentDescription = null,
                            modifier = Modifier.clickable { isDropdownExpanded = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDropdownExpanded = true }
                )

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    educationLevels.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level) },
                            onClick = {
                                selectedEducation = level
                                isDropdownExpanded = false
                                Toast.makeText(context, "Seleccionaste: $level", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank() || birthDate.isBlank()) {
                        Toast.makeText(
                            context,
                            "Por favor completa los campos obligatorios",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val intent = Intent(context, ContactDataActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            ) {
                Text("Siguiente")
            }
        }
    }
}

