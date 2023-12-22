package android.kotlinjetpack.pet

import android.kotlinjetpack.pet.data.ArticleModel
import android.kotlinjetpack.pet.data.MessageModel
import android.kotlinjetpack.pet.data.PostRekomendasiModel
import android.kotlinjetpack.pet.data.ReturnRekomendasiModel
import android.kotlinjetpack.pet.data.UserModel
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
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

class MyPetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           PetTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyPetScreen()
                }
            }
        }
    }
}

@Composable
fun MyPetScreen() {
    val context = LocalContext.current
    val questions = listOf(
        "Berapa harga biaya perawatan hewan peliharaan yang Anda inginkan?",
        "Berapa ukuran hewan peliharaan yang Anda inginkan?",
        "Dalam rentang berapa tingkat aktivitas hewan peliharaan yang Anda inginkan?",
        "Dalam rentang berapa tingkat agresivitas hewan peliharaan yang Anda inginkan?",
        "Dalam rentang berapa tingkat kesulitan perawatan hewan peliharaan yang Anda inginkan?",
        "Berapa rentang harga hewan peliharaan yang Anda inginkan?"
    )

    val answerChoicesPoint = listOf(
        listOf(0.0, 1.0, 2.0, 3.0, 4.0),
        listOf(4.0, 3.0, 2.0, 1.0, 0.0),
        listOf(4.0, 3.0, 2.0, 1.0, 0.0),
        listOf(4.0, 3.0, 2.0, 1.0, 0.0),
        listOf(4.0, 3.0, 2.0, 1.0, 0.0),
        listOf(0.0, 1.0, 2.0, 3.0, 4.0, 5.0)
    )

    val answerChoices = listOf(
        listOf("Sangat Mahal", "Mahal", "Sedang", "Murah", "Sangat Murah"),
        listOf("Sangat Besar", "Besar", "Sedang", "Kecil", "Sangat Kecil"),
        listOf("Sangat Aktif", "Aktif", "Sedang", "Pasif", "Sangat Pasif"),
        listOf("Sangat Agresif", "Agresif", "Sedang", "Jinak", "Sangat Jinak"),
        listOf("Sangat Sulit", "Sulit", "Sedang", "Mudah", "Sangat Mudah"),
        listOf("Rp. 0 - Rp. 100.000", "Rp. 100.000 – Rp. 500.000", "Rp. 500.000 – Rp. 1.000.000", "Rp. 1.000.000 – Rp. 5.000.000", "Rp. 5.000.000 – Rp. 10.000.000", "> Rp. 10.000.000")
    )

    val answers = remember {mutableStateListOf<String?>(null, null, null, null, null, null)}
    val answersIndex = remember {mutableStateListOf<Int?>(null, null, null, null, null, null)}

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswerIndex by remember { mutableStateOf(-1) }
    val isVisible = remember { mutableStateOf(true) }

    if(isVisible.value){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = questions[currentQuestionIndex])

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                answerChoices[currentQuestionIndex].forEachIndexed { index, answer ->
                    val isSelected = index == selectedAnswerIndex
                    Button(
                        onClick = {
                            answers[currentQuestionIndex] = answer
                            answersIndex[currentQuestionIndex] = index
                            selectedAnswerIndex = index
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = answer,
                            color = if (isSelected) Color.White else Color.Black
                        )
                    }
                }
            }

            if(answers[currentQuestionIndex] == null){
                Text(text = "Answer : ")
            }else{
                Text(text = "Answer : " + answers[currentQuestionIndex])
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = if(currentQuestionIndex != 0) Arrangement.SpaceBetween else Arrangement.End
            ) {
                if(currentQuestionIndex != 0){
                    Button(
                        onClick = {
                            if (currentQuestionIndex > 0) {
                                currentQuestionIndex--
                                selectedAnswerIndex = -1
                            }
                        }
                    ) {
                        Text(text = "Prev")
                    }
                }

                if(currentQuestionIndex == questions.size - 1){
                    Button(
                        onClick = {
                            isVisible.value = false
                        }
                    ) {
                        Text(text = "Done")
                    }
                }else if(currentQuestionIndex < questions.size - 1){
                    Button(
                        onClick = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                selectedAnswerIndex = -1
                            }
                        }
                    ) {
                        Text(text = "Next")
                    }
                }

            }
        }
    }else{
        val url = "https://backend-yqe4ddnmxq-de.a.run.app/api/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofitAPI = retrofit.create(RetrofitAPI::class.java)
        val dataModel = PostRekomendasiModel(
            answerChoicesPoint[0][answersIndex[0]!!],
            answerChoicesPoint[1][answersIndex[1]!!],
            answerChoicesPoint[2][answersIndex[2]!!],
            answerChoicesPoint[3][answersIndex[3]!!],
            answerChoicesPoint[4][answersIndex[4]!!],
            answerChoicesPoint[5][answersIndex[5]!!])
        val call: Call<ReturnRekomendasiModel?>? = retrofitAPI.postDataRekomendasi(dataModel)

        val rekomendasi = remember {mutableStateListOf<String?>(null, null, null, null, null, null, null, null, null, null)}

        call!!.enqueue(object : Callback<ReturnRekomendasiModel?> {
            override fun onResponse(call: Call<ReturnRekomendasiModel?>, response: Response<ReturnRekomendasiModel?>) {
                println(response.body())
                if(response.code() == 200){
                    val model: ReturnRekomendasiModel? = response.body()
                    println(model)
                    rekomendasi[0] = model!!.hewan1
                    rekomendasi[1] = model!!.hewan2
                    rekomendasi[2] = model!!.hewan3
                    rekomendasi[3] = model!!.hewan4
                    rekomendasi[4] = model!!.hewan5
                    rekomendasi[5] = model!!.hewan6
                    rekomendasi[6] = model!!.hewan7
                    rekomendasi[7] = model!!.hewan8
                    rekomendasi[8] = model!!.hewan9
                    rekomendasi[9] = model!!.hewan10
                }else if(response.code() == 400){
                    Toast.makeText(context, "Data tidak ada", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReturnRekomendasiModel?>, t: Throwable) {
                println("Error found is : " + t.message)
            }
        })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
        ) {
            Text(
                text = "Rekomendasi Hewan untuk Anda Adalah : ",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp)
            )
            LazyColumn{
                items(rekomendasi){ item ->
                    Text(
                        text = "- " + item.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyPetScreenPreview() {
    PetTheme {
        MyPetScreen()
    }
}