package com.example.savvy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.savvy.navigation.Navigation
import com.example.savvy.ui.theme.SavvyTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SavvyTheme {
                // A surface container using the 'background' color from the theme
                Navigation()
            }
        }
    }
}
