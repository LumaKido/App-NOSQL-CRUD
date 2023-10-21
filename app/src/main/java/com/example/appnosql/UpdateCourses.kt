package com.example.appnosql

import DBHandler
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appnosql.ui.theme.AppNoSqlTheme
import com.example.appnosql.ui.theme.PurpleGrey40


class UpdateCourses : ComponentActivity() {
    private lateinit var dbHandler: DBHandler // Declare a DBHandler instance


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = DBHandler(this) // Initialize the DBHandler instance

        // Recupere o parâmetro "courseName" da Intent.
        val courseNameToUpdate = intent.getStringExtra("courseName")

        setContent {
            AppNoSqlTheme {
                // on below line we are specifying background color for our application
                Surface(
                    // on below line we are specifying modifier and color for our app
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    // on the below line we are specifying the theme as the scaffold.
                    Scaffold(

                        // in scaffold we are specifying the top bar.
                        topBar = {

                            // inside top bar we are specifying background color.
                            TopAppBar(colors = TopAppBarDefaults.centerAlignedTopAppBarColors(),

                                // along with that we are specifying title for our top bar.
                                title = {

                                    // in the top bar we are specifying tile as a text
                                    Text(
                                        // on below line we are specifying
                                        // text to display in top app bar.
                                        text = "GFG",

                                        // on below line we are specifying
                                        // modifier to fill max width.
                                        modifier = Modifier.fillMaxWidth(),

                                        // on below line we are specifying
                                        // text alignment.
                                        textAlign = TextAlign.Center,

                                        // on below line we are specifying
                                        // color for our text.
                                        color = Color.White
                                    )
                                })
                        }) {
                        // on below line we are calling our method to display UI
                        UpdateDataInDatabase(LocalContext.current)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateDataInDatabase(
    context: Context
) {
    val courseNameFromIntent = (context as Activity).intent.getStringExtra("courseName")
    val courseDuration = remember { mutableStateOf(TextFieldValue()) }
    val courseTracks = remember { mutableStateOf(TextFieldValue()) }
    val courseDescription = remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val dbHandler = DBHandler(context)

        Text(
            text = "SQLite Database in Android",
            color = PurpleGrey40,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = courseDuration.value,
            onValueChange = { courseDuration.value = it },
            placeholder = { Text(text = "Enter your course duration") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = courseTracks.value,
            onValueChange = { courseTracks.value = it },
            placeholder = { Text(text = "Enter your course tracks") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = courseDescription.value,
            onValueChange = { courseDescription.value = it },
            placeholder = { Text(text = "Enter your course description") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black, fontSize = 15.sp),
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            val courseDurationText = courseDuration.value.text
            val courseDescriptionText = courseDescription.value.text
            val courseTracksText = courseTracks.value.text

            val success = dbHandler.updateCourse(
                courseNameFromIntent ?: "", // Use courseNameFromIntent se não for nulo, caso contrário, use uma string vazia
                courseDurationText,
                courseDescriptionText,
                courseTracksText
            )


            if (success) {
                Toast.makeText(context, "Course Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to Update Course", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Update Course", color = Color.White)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {
            val intent = Intent(context, ViewCourses::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Read Courses to Database", color = Color.White)
        }
    }
}
