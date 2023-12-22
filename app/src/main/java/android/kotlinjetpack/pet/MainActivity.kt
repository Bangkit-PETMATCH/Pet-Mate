package android.kotlinjetpack.pet

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.kotlinjetpack.pet.ui.theme.PetTheme
import android.os.Handler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen{
                        navigateToDataScreen()
                    }
                }
            }
        }
    }

    private fun navigateToDataScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun SplashScreen(navigateToNextScreen: () -> Unit) {
    val customFontFamily = FontFamily(
        Font(R.font.krabuler) // Replace 'your_font' with the name of your font file
    )

    LaunchedEffect(true) {
        // Simulate a delay of 2 seconds
        delaySplashScreen(2000) {
            navigateToNextScreen()
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Replace R.drawable.your_logo with your actual logo resource
            Image(
                painter = painterResource(id = R.drawable.logo_pet),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(5.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "PetMate",
                fontSize = 24.sp,
                fontFamily = customFontFamily,
                color = Color(0xFF538AA8),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

fun delaySplashScreen(duration: Long, navigateToNextScreen: () -> Unit) {
    Handler().postDelayed({
        navigateToNextScreen()
    }, duration)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PetTheme {
        SplashScreen(navigateToNextScreen = {})
    }
}