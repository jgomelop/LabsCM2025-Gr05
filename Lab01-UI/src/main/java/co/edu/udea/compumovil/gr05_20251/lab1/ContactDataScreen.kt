package co.edu.udea.compumovil.gr05_20251.lab1

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactDataScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var phone by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var country by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }

    val countryOptions = stringArrayResource(R.array.paises_latinoamerica).toList()
    val cityOptions = stringArrayResource(R.array.ciudades_colombia).toList()

    val errMsg = stringResource(R.string.msg_required_fields_missing)
    val tituloContacto = stringResource(R.string.titulo_contacto)
    val etiquetaTelefono = stringResource(R.string.telefono_label)
    val etiquetaDireccion = stringResource(R.string.direccion_label)
    val etiquetaEmail = stringResource(R.string.email_tag)
    val etiquetaPais = stringResource(R.string.pais_label)
    val etiquetaCiudad = stringResource(R.string.ciudad_label)
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = tituloContacto,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text(stringResource(R.string.telefono_label)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text(stringResource(R.string.direccion_label)) },
            keyboardOptions = KeyboardOptions(autoCorrect = false),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email_tag)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        AutoCompleteTextField(
            label = stringResource(R.string.pais_label),
            value = country,
            onValueChange = { country = it },
            options = countryOptions
        )

        Spacer(modifier = Modifier.height(8.dp))

        AutoCompleteTextField(
            label = stringResource(R.string.ciudad_label),
            value = city,
            onValueChange = { city = it },
            options = cityOptions
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (phone.isBlank() || email.isBlank() || country.isBlank()) {
                Toast.makeText(
                    context,
                    errMsg,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                println(tituloContacto)
                println("$etiquetaTelefono $phone")
                println("$etiquetaDireccion $address")
                println("$etiquetaEmail $email")
                println("$etiquetaPais $country")
                println("$etiquetaCiudad $city")
            }
        }) {
            Text(stringResource(R.string.btn_finalizar))
        }
    }
}

