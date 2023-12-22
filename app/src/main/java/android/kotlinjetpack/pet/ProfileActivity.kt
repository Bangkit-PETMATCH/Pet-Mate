package android.kotlinjetpack.pet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlinjetpack.pet.data.MessageLoginModel
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.compose.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : ComponentActivity() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)

            PetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen(sharedPreferences)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(sharedPreferences: SharedPreferences) {
    val customFontFamily = FontFamily(
        Font(R.font.krabuler) // Replace 'your_font' with the name of your font file
    )

    val context = LocalContext.current
    var username = remember { mutableStateOf(TextFieldValue()) }
    var email = remember { mutableStateOf(TextFieldValue()) }
    val response = remember { mutableStateOf("") }
    val id = sharedPreferences.getInt("id", 0)

    postDataUsingRetrofit(context, username, email, response, id)

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
                text = "Profile User",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp),
                fontFamily = customFontFamily
            )
            Spacer(modifier = Modifier.height(50.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Emmail") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                readOnly = true
            )
            Button(
                onClick = {
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 20.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Logout")
            }
        }
    }
}

private fun postDataUsingRetrofit(
    ctx: Context,
    username: MutableState<TextFieldValue>,
    email: MutableState<TextFieldValue>,
    result: MutableState<String>,
    id: Int
) {
    val url = "https://backend-yqe4ddnmxq-de.a.run.app/profile/"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val call: Call<UserModel?>? = retrofitAPI.dataProfile(id)

    call!!.enqueue(object : Callback<UserModel?> {
        override fun onResponse(call: Call<UserModel?>, response: Response<UserModel?>) {
            if(response.code() == 200){
                val model: UserModel? = response.body()
                username.value = TextFieldValue(text = model!!.username)
                email.value = TextFieldValue(text = model!!.email)
            }else if(response.code() == 400){
                Toast.makeText(ctx, "Silahkan login dulu", Toast.LENGTH_SHORT).show()
                val intent = Intent(ctx, LoginActivity::class.java);
                ctx.startActivity(intent)
            }
        }

        override fun onFailure(call: Call<UserModel?>, t: Throwable) {
            result.value = "Error found is : " + t.message
        }
    })
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    PetTheme {
        ProfileScreen(sharedPreferences)
    }
}