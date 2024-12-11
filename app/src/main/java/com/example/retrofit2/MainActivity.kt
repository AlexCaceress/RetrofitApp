package com.example.retrofit2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.network.HttpException
import com.example.retrofit2.models.Meme
import com.example.retrofit2.screens.DetailScreen
import com.example.retrofit2.screens.MainScreen
import com.example.retrofit2.ui.theme.Retrofit2Theme
import com.example.retrofit2.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Retrofit2Theme {

                val navController = rememberNavController()
                var memeList by remember {
                    mutableStateOf(listOf<Meme>())
                }

                val scope = rememberCoroutineScope()

                LaunchedEffect(key1 = true) {
                    scope.launch(Dispatchers.IO) {
                        val response = try {
                            RetrofitInstance.api.getMemesList()
                        } catch (e: IOException) {
                            Toast.makeText(
                                this@MainActivity,
                                "app error ${e.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        } catch (e: HttpException) {
                            Toast.makeText(
                                this@MainActivity,
                                "http error ${e.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@launch
                        }

                        if (response.isSuccessful && response.body() != null) {
                            withContext(Dispatchers.Main) {
                                memeList = response.body()!!.data.memes
                                Log.e("peticion", memeList.toString())
                            }
                        }

                    }
                }

                NavHost(navController = navController, startDestination = "MainScreen") {

                    composable(route = "MainScreen") {
                        MainScreen(memesList = memeList, navController = navController)
                    }
                    composable(
                        route = "DetailScreen?name={name}&url={url}",
                        arguments = listOf(
                            navArgument(name = "name") {
                                type = NavType.StringType
                            },
                            navArgument(name = "url") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        DetailScreen(
                            name = it.arguments?.getString("name"),
                            url = it.arguments?.getString("url")
                        )
                    }

                }


            }
        }
    }
}