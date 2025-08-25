package com.ayokerjo.demogenci.Product

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ayokerjo.demogenci.ApiClient
import com.ayokerjo.demogenci.R
import com.ayokerjo.demogenci.api.model.Product
import com.ayokerjo.demogenci.api.model.ProductResponse
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailProductActivity : AppCompatActivity() {

    private lateinit var tTitle: TextView
    private lateinit var tDesc: TextView
    private lateinit var ivThumb: ImageView
    private lateinit var aidi: String
    private lateinit var call: Call<Product>
    private lateinit var linearContent: LinearLayout
    private lateinit var loadingPB: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        linearContent = findViewById(R.id.linearContent)
        loadingPB = findViewById(R.id.loadingPB)

        tTitle = findViewById(R.id.textTitle)
        tDesc = findViewById(R.id.textDesc)
        ivThumb = findViewById(R.id.thumbnailnya)

        aidi = intent.getStringExtra("aidi").toString()

        loadingPB.visibility = View.VISIBLE

        call = ApiClient.productService.getProductById(aidi.toIntOrNull())
        call.enqueue(object : Callback<Product>{
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                Log.e("resSuk", response.body().toString())

                loadingPB.visibility = View.GONE
                linearContent.visibility = View.VISIBLE

                if(response.isSuccessful){
                    tTitle.text = response.body()?.title
                    tDesc.text = response.body()?.description

                    Glide.with(applicationContext).load(response.body()?.thumbnail).centerCrop().into(ivThumb)
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                loadingPB.visibility = View.GONE

                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
                Log.e("resFail", t.localizedMessage.toString())
            }

        })
    }
}