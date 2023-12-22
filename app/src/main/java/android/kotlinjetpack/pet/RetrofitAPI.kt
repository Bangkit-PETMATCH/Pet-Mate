package android.kotlinjetpack.pet

import android.kotlinjetpack.pet.data.ArticleModel
import android.kotlinjetpack.pet.data.MessageLoginModel
import android.kotlinjetpack.pet.data.MessageModel
import android.kotlinjetpack.pet.data.PostRekomendasiModel
import android.kotlinjetpack.pet.data.ReturnRekomendasiModel
import android.kotlinjetpack.pet.data.UserLoginModel
import android.kotlinjetpack.pet.data.UserModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitAPI {
    @POST("register")
    fun postData(@Body userModel: UserModel?): Call<MessageModel?>?

    @POST("login")
    fun postDataLogin(@Body userModel: UserLoginModel?): Call<MessageLoginModel?>?

    @GET("{id}")
    fun dataProfile(@Path("id") postData: Int?): Call<UserModel?>?

    @GET("articles")
    fun dataArticles(): Call<List<ArticleModel>>

    @POST("predict")
    fun postDataRekomendasi(@Body model: PostRekomendasiModel?): Call<ReturnRekomendasiModel?>?
}