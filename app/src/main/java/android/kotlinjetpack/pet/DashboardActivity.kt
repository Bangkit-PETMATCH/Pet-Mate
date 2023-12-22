package android.kotlinjetpack.pet

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.kotlinjetpack.pet.data.ArticleModel
import android.kotlinjetpack.pet.data.MessageLoginModel
import android.kotlinjetpack.pet.data.UserLoginModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.kotlinjetpack.pet.ui.theme.PetTheme
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           PetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataScreen()
                }
            }
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun DataScreen() {
    val context = LocalContext.current
    var articles by remember { mutableStateOf<List<ArticleModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        val url = "https://backend-yqe4ddnmxq-de.a.run.app/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val call: Call<List<ArticleModel>> = retrofitAPI.dataArticles()

        call!!.enqueue(object : Callback<List<ArticleModel>?> {
            override fun onResponse(call: Call<List<ArticleModel>?>, response: Response<List<ArticleModel>?>) {
                if(response.code() == 200){
                    articles = response.body() ?: emptyList()
                }else if(response.code() == 400){
                    Toast.makeText(context, "Username atau password tidak valid", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ArticleModel>?>, t: Throwable) {
                println("Error found is : " + t.message)
            }
        })
    }

    val imageUrls = listOf(
        R.drawable.kucing1,
        R.drawable.kucing2,
        R.drawable.kucing3
    ).map { painterResource(it) }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        3
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp)
    ) {
        HorizontalPager(state = pagerState) {page ->
            Image(
                painter = imageUrls[page],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        // List of data
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            LazyColumn {
                items(articles) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp, bottom = 16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val painter = rememberImagePainter(
                                data = item.images
                            )
                            // Left side image
                            Image(
                                painter = painter,
                                contentDescription = null,
                                modifier = Modifier
                                    .height(150.dp)
                                    .width(150.dp),
                                contentScale = ContentScale.Crop
                            )

                            // Right side content
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth()
                                    .padding(start = 16.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = item.judul_artikel)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = item.kategori)
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    val intent = Intent(context, DetailArticleActivity::class.java)
                                    intent.putExtra("images", item.images)
                                    intent.putExtra("judul_artikel", item.judul_artikel)
                                    intent.putExtra("kategori", item.kategori)
                                    intent.putExtra("jenis_hewan", item.jenis_hewan)
                                    intent.putExtra("isi_artikel", item.isi_artikel)
                                    context.startActivity(intent)
                                }) {
                                    Text(text = "Detail Article")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DataPreview() {
    PetTheme {
        DataScreen()
    }
}