package com.ayokerjo.demogenci.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayokerjo.demogenci.R
import com.ayokerjo.demogenci.api.model.Product
import com.bumptech.glide.Glide

class ProductAdapter(
    private val onClick: (Product) -> Unit) : ListAdapter<Product,
        ProductAdapter.ProductViewHolder>(ProductCallback){

    class  ProductViewHolder(itemView: View, val onClick: (Product) -> Unit) :
        RecyclerView.ViewHolder(itemView){

        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val brand: TextView = itemView.findViewById(R.id.brand)
        private val price: TextView = itemView.findViewById(R.id.price)

        private var currentProduct: Product? = null

        init {
            itemView.setOnClickListener{
                currentProduct?.let {
                    onClick(it)
                }
            }
        }

        fun bind(product: Product){
            currentProduct = product

            title.text = product.title
            brand.text = product.brand
            price.text = product.price.toString()

            Glide.with(itemView).load(product.thumbnail).centerCrop().into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

object ProductCallback : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return  oldItem.id == newItem.id
    }

}