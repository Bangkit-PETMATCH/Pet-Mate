package android.kotlinjetpack.pet

import android.content.Context
import android.content.Intent
import android.kotlinjetpack.pet.data.MessageModel
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "register") {
                composable("register") {
                    PetTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RegisterScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val customFontFamily = FontFamily(
        Font(R.font.krabuler) // Replace 'your_font' with the name of your font file
    )

    val context = LocalContext.current
    var email = remember { mutableStateOf(TextFieldValue()) }
    var username = remember { mutableStateOf(TextFieldValue()) }
    var password = remember { mutableStateOf(TextFieldValue()) }
    var passwordConfirm = remember { mutableStateOf(TextFieldValue()) }
    var response = remember { mutableStateOf("") }

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
                text = "Register",
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
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(onNext = { /* Handle next action */ })
            )
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
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
            OutlinedTextField(
                value = passwordConfirm.value,
                onValueChange = { passwordConfirm.value = it },
                label = { Text("Password Confirm") },
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
                    if(password.value.text == passwordConfirm.value.text){
                        postDataUsingRetrofit(context, email, username, password, response);
                    }else{
                        Toast.makeText(context, "Password dan Password Confirm tidak sama.", Toast.LENGTH_SHORT).show();
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 20.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Register")
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
                    text = "Already have account?",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFontFamily,

                    )
                TextButton(
                    onClick = {
                        val intent = Intent(context, LoginActivity::class.java);
                        context.startActivity(intent)
                    },
                ) {
                    Text(
                        text = "Login here",
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
    email: MutableState<TextFieldValue>,
    username: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    result: MutableState<String>
) {
    var url = "https://backend-yqe4ddnmxq-de.a.run.app/auth/"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
    val dataModel = UserModel(null, email.value.text, username.value.text, password.value.text)
    val call: Call<MessageModel?>? = retrofitAPI.postData(dataModel)

    call!!.enqueue(object : Callback<MessageModel?> {
        override fun onResponse(call: Call<MessageModel?>, response: Response<MessageModel?>) {
            val model: MessageModel? = response.body()
            result.value = model!!.message
            Toast.makeText(ctx, result.value, Toast.LENGTH_SHORT).show()
            if(response.code() == 200){
                val intent = Intent(ctx, LoginActivity::class.java);
                ctx.startActivity(intent)
            }
        }

        override fun onFailure(call: Call<MessageModel?>, t: Throwable) {
            result.value = "Error found is : " + t.message
        }
    })
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    PetTheme {
        RegisterScreen(navController = rememberNavController())
    }
}