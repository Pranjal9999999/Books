package com.pranjal.books.books.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.pranjal.books.R
import com.pranjal.books.books.adapter.FavoritesRecyclerAdapter
import com.pranjal.books.books.database.BookDatabase
import com.pranjal.books.books.database.BookEntity
import kotlinx.android.synthetic.main.fragment_favourites.*

class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourites:RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager:RecyclerView.LayoutManager
    lateinit var recyclerAdapter:FavoritesRecyclerAdapter
    var dbBookList=listOf<BookEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this com.pranjal.books.activity.fragment
        val view= inflater.inflate(R.layout.fragment_favourites, container, false)
        recyclerFavourites=view.findViewById(R.id.recyclerFavourite)
        progressBar=view.findViewById(R.id.progress_bar)
        layoutManager=GridLayoutManager(
            activity as Context,2)
        dbBookList=RetrieveFavourites(activity as Context).execute().get()
        if(activity!=null)
        {
            progressBar.visibility=View.GONE
            recyclerAdapter= FavoritesRecyclerAdapter(activity as Context,dbBookList)
            recyclerFavourites.adapter=recyclerAdapter
            recyclerFavourites.layoutManager=layoutManager
        }
        return view
    }
    class RetrieveFavourites(val context:Context):AsyncTask<Void,Void,List<BookEntity>>() {
        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context, BookDatabase::class.java,"book_db").build()
            return db.bookDao().getAllBooks()

        }

    }

}
