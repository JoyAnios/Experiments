package io.github.udpandroidclient

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.widget.TextViewCompat
import io.github.udpandroidclient.ui.theme.UDPAndroidClientTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UDPAndroidClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    UDPConnectPanel()
                }
            }
        }
    }
}

@Composable
fun UDPConnectPanel() {

    var ip by remember {
        mutableStateOf("10.0.2.2")
    }
    var port by remember {
        mutableStateOf("4399")
    }

    var data by remember {
        mutableStateOf("Some message")
    }

    var receivedData by remember {
        mutableStateOf("")
    }

    var scrollState = rememberScrollState();
    val scaffoldState = rememberScaffoldState();
    val scope = rememberCoroutineScope();

    Scaffold(
        scaffoldState = scaffoldState,

        ) {
        Card(modifier = Modifier.padding(all = 10.dp)) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(10.dp)
            ) {

                Row() {
                    OutlinedTextField(value = ip, onValueChange = {
                        ip = it
                    }, label = { Text(text = "IP Address") })

                    OutlinedTextField(
                        value = port,
                        onValueChange = {
                            port = it
                        },
                        label = {
                            Text(text = "Port")
                        },
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = data,
                    onValueChange = { data = it },
                    label = { Text(text = "Data") })

                Card(
                    modifier = Modifier
                        .heightIn(max = 100.dp)
                        .scrollable(scrollState, orientation = Orientation.Vertical)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = receivedData,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(text = "Server response") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Sharp.Close,
                                    contentDescription = "Clear",
                                    modifier = Modifier.clickable { receivedData = "" }
                                )
                            })
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                val c = UDPClient(ip, port)
                                val t = async(Dispatchers.IO) {
                                    c.send(data);
                                }
                                t.await()
                                val r = async(Dispatchers.IO) {
                                    receivedData += "${c.receive()}\n"
                                }
                                r.await()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(text = "Send")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                scan(4399)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text(text = "Scan")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UDPAndroidClientTheme {
        UDPConnectPanel()
    }
}