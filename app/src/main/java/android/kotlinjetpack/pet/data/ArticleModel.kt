package android.kotlinjetpack.pet.data

data class ArticleModel(
    var id: Int,
    var judul_artikel: String,
    var kategori: String,
    var jenis_hewan: String,
    var isi_artikel: String,
    var images: String
)