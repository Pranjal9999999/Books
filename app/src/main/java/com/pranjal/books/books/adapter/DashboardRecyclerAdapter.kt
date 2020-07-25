package com.pranjal.books.books.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.pranjal.books.R
import com.pranjal.books.books.activity.Description
import com.pranjal.books.books.model.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, val itemList:ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>(){
    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        var txtBookName:TextView=view.findViewById(R.id.txtList)
        var imgBookImage:ImageView=view.findViewById(R.id.imgBook)
        var txtBookRating:TextView=view.findViewById(R.id.rating)
        var txtAuthor:TextView=view.findViewById(R.id.txtAuthor)
        var txtBookPrice:TextView=view.findViewById(R.id.txtPrice)
        val llContent:ViewGroup= view.findViewById(R.id.llContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder
    {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.single_row_dashboard,parent,false)
  return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book=itemList[position]
        holder.txtBookName.text=book.bookName
        holder.txtAuthor.text=book.bookAuthor
        Picasso.get().load(book.bookImage).error(R.drawable.book).into(holder.imgBookImage)
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.rating

        holder.llContent.setOnClickListener{
   Toast.makeText( context,"Clicked on ${holder.txtBookName.text} ",Toast.LENGTH_SHORT).show()
    val intent= Intent(context,Description::class.java)
            intent.putExtra( "book",book.bookId)
            context.startActivity(intent)
}

    }


}