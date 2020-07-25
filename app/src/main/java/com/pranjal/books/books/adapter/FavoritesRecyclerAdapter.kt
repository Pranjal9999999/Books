package com.pranjal.books.books.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pranjal.books.R
import com.pranjal.books.books.database.BookEntity
import com.squareup.picasso.Picasso


class FavoritesRecyclerAdapter (val context: Context,val bookList: List<BookEntity>):RecyclerView.Adapter<FavouriteViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteViewHolder {


        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourite_single_row,parent,false)

    return FavouriteViewHolder( view)}

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(
        holder: FavouriteViewHolder,
        position: Int
    ) {
        val book=bookList[position]
        holder.txtBookName.text=book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.book).into(holder.imgBookImage)

    }


}
 class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view)
{
     val txtBookName:TextView=view.findViewById(R.id.bookTitle)
    val txtBookAuthor:TextView=view.findViewById(R.id.bookAuthor)
    val txtBookPrice:TextView=view.findViewById(R.id.bookPrice)
    val txtBookRating:TextView=view.findViewById(R.id.bookRating)
    val imgBookImage:ImageView=view.findViewById(R.id.imgFavBook)
    val llContent:LinearLayout=view.findViewById(R.id.llContent)
}
