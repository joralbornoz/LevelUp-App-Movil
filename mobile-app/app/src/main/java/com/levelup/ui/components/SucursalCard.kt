package com.levelup.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.levelup.data.Sucursal

@Composable
fun SucursalCard(sucursal: Sucursal) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = sucursal.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(text = "Dirección: ${sucursal.direccion}, ${sucursal.ciudad}")
            Text(text = "Horario: ${sucursal.horario}")
            Text(text = "Teléfono: ${sucursal.telefono}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=${Uri.encode(sucursal.direccion)}"))
                    context.startActivity(mapIntent)
                }) {
                    Text("Ver mapa")
                }

                OutlinedButton(onClick = {
                    val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${sucursal.telefono}"))
                    context.startActivity(callIntent)
                }) {
                    Text("Llamar")
                }
            }
        }
    }
}

