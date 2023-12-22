package android.kotlinjetpack.pet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlinjetpack.pet.data.MessageLoginModel
import android.kotlinjetpack.pet.data.MessageModel
import android.kotlinjetpack.pet.data.UserLoginModel
import android.kotlinjetpack.pet.data.UserModel
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
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL
import java.time.format.TextStyle

class LoginActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    PetTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoginScreen(navController = navController, sharedPreferences)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, sharedPreferences: SharedPreferences) {
    val customFontFamily = FontFamily(
        Font(R.font.krabuler) // Replace 'your_font' with the name of your font file
    )

    val context = LocalContext.current
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    val response = remember { mutableStateOf("") }

    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(30.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_pet),
                contentDescription = "Logo",
                modifier = Modifier.size(96.dp)
            )
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = customFontFamily
            )
            Text(
                text = "Input your Username and Password",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp),
                fontFamily = customFontFamily
            )
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = { /* Handle next action */ })
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onDone = { /* Handle done action */ }),
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { /* Handle visibility toggle */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_visibility_24),
                            contentDescription = null
                        )
                    }
                }
            )
            Button(
                onClick = {
                    postDataUsingRetrofit(context, username, password, response, sharedPreferences)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 20.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Login")
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have account?",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFontFamily,

                )
                TextButton(
                    onClick = {
                        val intent = Intent(context, RegisterActivity::class.java);
                        context.startActivity(intent)
                    },
                ) {
                    Text(
                        text = "Register here",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = customFontFamily,
                    )
                }
            }
        }
    }
}

private fun postDataUsingRetrofit(
    ctx: Context,
    username: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    result: MutableState<String>,
    sharedPreferences: SharedPreferences
) {
    val url = "https://backend-yqe4ddnmxq-de.a.run.app/auth/"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val dataModel = UserLoginModel(username.value.text, password.value.text)
    val call: Call<MessageLoginModel?>? = retrofitAPI.postDataLogin(dataModel)

    call!!.enqueue(object : Callback<MessageLoginModel?> {
        override fun onResponse(call: Call<MessageLoginModel?>, response: Response<MessageLoginModel?>) {
            if(response.code() == 200){
                val model: MessageLoginModel? = response.body()
                result.value = model!!.message
                Toast.makeText(ctx, result.value, Toast.LENGTH_SHORT).show()

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                model.id?.let { editor.putInt("id", it) }
                editor.apply()

                val intent = Intent(ctx, MainContentActivity::class.java);
                ctx.startActivity(intent)
            }else if(response.code() == 400){
                Toast.makeText(ctx, "Username atau password tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<MessageLoginModel?>, t: Throwable) {
            result.value = "Error found is : " + t.message
        }
    })
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    PetTheme {
        LoginScreen(navController = rememberNavController(), sharedPreferences)
    }
}