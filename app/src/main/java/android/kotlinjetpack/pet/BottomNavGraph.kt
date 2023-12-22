package android.kotlinjetpack.pet

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavGraph(navController: NavHostController, paddingValues: PaddingValues){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    NavHost(navController = navController, startDestination = BottomBarScreen.Beranda.route){
        composable(route = BottomBarScreen.Beranda.route){
            DataScreen()
        }
        composable(route = BottomBarScreen.MyPet.route){
            MyPetScreen()
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen(sharedPreferences)
        }
    }
}