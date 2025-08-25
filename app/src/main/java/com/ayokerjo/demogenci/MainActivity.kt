package com.ayokerjo.demogenci

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ayokerjo.demogenci.Product.DetailProductActivity
import com.ayokerjo.demogenci.api.adapter.ProductAdapter
import com.ayokerjo.demogenci.api.model.Product
import com.ayokerjo.demogenci.api.model.ProductResponse
import com.ayokerjo.demogenci.data.local.AppDatabase
import com.ayokerjo.demogenci.data.local.ProductMapper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var call: Call<ProductResponse>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var linearLay: LinearLayout
    private lateinit var etSearch: EditText
    private var allProducts: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        linearLay = findViewById(R.id.framePulldown)

        etSearch = findViewById(R.id.etSearch)

        swipeRefresh = findViewById(R.id.refreshLayout)
        recyclerView = findViewById(R.id.recycler_view)

        productAdapter = ProductAdapter { product -> productOnClick(product) }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        swipeRefresh.setOnRefreshListener {
            getData()
        }

        getData()

        etSearch.addTextChangedListener { text ->
            val keyword = text.toString()
            val filtered = if (keyword.isEmpty()) {
                allProducts
            } else {
                allProducts.filter { it.title.contains(keyword, ignoreCase = true) }
            }

            productAdapter.submitList(filtered)
        }
    }

    private fun productOnClick(product: Product){
//        Toast.makeText(applicationContext, product.description, Toast.LENGTH_LONG).show()
//        Toast.makeText(applicationContext, product.id.toString(), Toast.LENGTH_LONG).show()
        val intent = Intent(this, DetailProductActivity::class.java)
        intent.putExtra("aidi", product.id.toString())
        startActivity(intent)
    }

    private fun getData(){
        swipeRefresh.isRefreshing = true

        call = ApiClient.productService.getAll()
        call.enqueue(object : Callback<ProductResponse>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                swipeRefresh.isRefreshing = false

                if (response.isSuccessful) {
                    allProducts = response.body()?.products ?: emptyList()

                    // simpan ke Room
                    lifecycleScope.launch {
                        val db = AppDatabase.getDatabase(this@MainActivity)
                        val allProducts = response.body()?.products ?: emptyList()
                        val entities = ProductMapper.fromApiListToEntityList(allProducts)
                        db.productDao().insertAll(entities)
                    }

                    productAdapter.submitList(allProducts)
                }

                linearLay.visibility = View.GONE
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                swipeRefresh.isRefreshing = false
//                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
                Toast.makeText(applicationContext, "ambil data dari Room", Toast.LENGTH_LONG).show()
                t.localizedMessage?.let { Log.e("cekhere", it) }

                // ambil data dari Room
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(this@MainActivity)
                    val localData = db.productDao().getAll()

                    // mapping dari ProductEntity -> Product
                    val mappedData = localData.map { entity ->
                        Product(
                            id = entity.id,
                            title = entity.title,
                            description = entity.description,
                            brand = entity.brand.toString(),
                            price = entity.price,
                            stock = entity.stok,
                            thumbnail = entity.thumbnail
                        )
                    }

                    allProducts = mappedData
                    productAdapter.submitList(allProducts)
                }

                linearLay.visibility = View.GONE
            }

        })
    }
}