@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.timezone

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timezone.ui.theme.TimeZoneTheme
import java.time.ZoneId
import java.time.ZonedDateTime

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimeZoneTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

data class Country(val name: String, val zoneId: ZoneId, val mx: Int)

@RequiresApi(Build.VERSION_CODES.O)
fun currentTimeAt(location: String, zoneId: ZoneId): String {
    val zonedDateTime = ZonedDateTime.now(zoneId)
    val localTime = zonedDateTime.toLocalTime()
    return "The time in $location is ${localTime.hour}:${localTime.minute}:${localTime.second}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun countries() = listOf(
    Country("Japan", ZoneId.of("Asia/Tokyo"), R.drawable.jp),
    Country("France", ZoneId.of("Europe/Paris"), R.drawable.fr),
    Country("Mexico", ZoneId.of("America/Mexico_City"), R.drawable.mx),
    Country("Indonesia", ZoneId.of("Asia/Jakarta"), R.drawable.id),
    Country("Egypt", ZoneId.of("Africa/Cairo"), R.drawable.eg),
    Country("India", ZoneId.of("Asia/Kolkata"), R.drawable.ind)
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun App(countries: List<Country> = countries()) {
    var showCountries by remember { mutableStateOf(false) }
    var timeAtLocation by remember { mutableStateOf("No Location Selected") }

    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = timeAtLocation,
            style = TextStyle(fontSize = 20.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box {
            Button(
                onClick = { showCountries = !showCountries }
            ) {
                Text(text = "Select Location")
            }

            DropdownMenu(
                expanded = showCountries,
                onDismissRequest = { showCountries = false },
                modifier = Modifier.width(150.dp) // Set width to match button or desired size
            ) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = country.mx),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(end = 10.dp),
                                    contentDescription = "${country.name} flag"
                                )
                                Text(text = country.name)
                            }
                        },
                        onClick = {
                            timeAtLocation = currentTimeAt(country.name, country.zoneId)
                            showCountries = false
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TimeZoneTheme {
        App()
    }
}
