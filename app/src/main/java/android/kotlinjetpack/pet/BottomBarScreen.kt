package android.kotlinjetpack.pet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
){
    object Beranda : BottomBarScreen(
        route = "beranda",
        title = "Home",
        icon = R.drawable.home
    )
    object MyPet : BottomBarScreen(
        route = "mypet",
        title = "MyPet",
        icon = R.drawable.pets
    )
    object Profile : BottomBarScreen(
        route = "profile",
        title = "Profile",
        icon = R.drawable.profile_user
    )
}
