package com.pranjal.books.books.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pranjal.books.R
import com.pranjal.books.books.database.BookDatabase
import com.pranjal.books.books.database.BookEntity
import com.pranjal.books.books.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_description.*
import org.json.JSONObject
import java.lang.Exception


class Description : AppCompatActivity() {
    lateinit var bookName: TextView
    lateinit var bookAuthor: TextView
    lateinit var rating: TextView
    lateinit var bookPrice: TextView
    lateinit var bookImage: ImageView
    lateinit var content: TextView
    var bookId = "100"
    lateinit var progressBar: ProgressBar
    lateinit var addToFavBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        bookAuthor = findViewById(R.id.txtAuthor)
        bookImage = findViewById(R.id.imgBook)
        bookName = findViewById(R.id.txtList)
        bookPrice = findViewById(R.id.txtPrice)
        content = findViewById(R.id.scrollContent)
        rating = findViewById(R.id.rating)
        progressBar = findViewById(R.id.progress_circular)
        addToFavBtn = findViewById(R.id.btnAdd)
        var toolbar:Toolbar
        title = "Book Details"
        progressBar.visibility = View.VISIBLE
        if (intent != null) {
            bookId = intent.getStringExtra("book")
        } else {
            finish()
            Toast.makeText(this@Description, "Some unexpected error occured", Toast.LENGTH_LONG)
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this@Description, "Some unexpected error occured", Toast.LENGTH_LONG)
        }
        val queue = Volley.newRequestQueue(this@Description)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)
        if (ConnectionManager().checkConnectivity(this@Description)) {
            val jsonRequest = object : JsonObjectRequest(
                Request.Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressBar.visibility = View.GONE
                            val bookImageUrl=bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image")).into(imgBook)
                            bookName.text = bookJsonObject.getString("name")
                            bookAuthor.text = bookJsonObject.getString("author")

                            bookPrice.text = bookJsonObject.getString("price")

                            rating.text = bookJsonObject.getString("rating")
                            content.text= bookJsonObject.getString("description")
                            val bookEntity=BookEntity(
                            bookId?.toInt() ,
                            bookName.text.toString(),
                            bookAuthor.text.toString(),
                            bookPrice.text.toString(),
                            rating.text.toString(),
                            content.text.toString(),
                            bookImageUrl
                            )
                            val checkFav=DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav=checkFav.get()
                            if (isFav)
                            {
                                addToFavBtn.text="Remove From Favourites"

                            }
                            else
                            {
                                addToFavBtn.text="Add to Favourites"
                                val favColor =ContextCompat.getColor(applicationContext,R.color.FavouriteBook)
                                addToFavBtn.setBackgroundColor(favColor)

                            }
                            addToFavBtn.setOnClickListener {
                                if (!DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                        .get())
                                 {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@Description,
                                            "Added to  favourites",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        addToFavBtn.text = "Remove From Favourites"
                                    } else {
                                        android.widget.Toast.makeText(
                                            this@Description,
                                            "Some ERROR OCCURED",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()

                                    }


                                } else {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@Description,
                                            "Remove from  favourites",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        addToFavBtn.text = "Add to  Favourites"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.FavouriteBook
                                        )
                                        addToFavBtn.setBackgroundColor(favColor)
                                    }else
                                    {Toast.makeText(
                                        this@Description,
                                        "Some ERROR OCCURED",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    }


                                }
                            }} else
                            Toast.makeText(
                                this@Description,
                                "Some ERROR OCCURED",
                                Toast.LENGTH_LONG
                            ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@Description, "Some ERROR OCCURED", Toast.LENGTH_LONG)
                            .show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@Description, "Volley ERRor $it", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "56030a473bfee6"
                    return headers
                }
            }
            queue.add(jsonRequest)
        } else {
            val dialog = AlertDialog.Builder(this@Description)
            dialog.setTitle("ERROR")
            dialog.setMessage(" No Internet Connection Found")
            dialog.setNegativeButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setPositiveButton("Exit")
            { text, listener ->
                ActivityCompat.finishAffinity(this@Description)
            }
            // INTERNET
            dialog.create()
            dialog.show()
        }


    }
    class DBAsyncTask(val context: Context,val bookEntity: BookEntity, val mode:Int): AsyncTask<Void, Void, Boolean>(){
        //mode 1->Check db if the book is favourite or not
        //mod2-> save teh book in db as favourites
        //mod->3 delete the book from favourites
        val db= Room.databaseBuilder(context,BookDatabase::class.java,"book_db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode)
            {
                1->
                {
                    val book:BookEntity?=db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book!=null

                }
                2->
                {
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->
                {
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }

            }
            return false
        }

    }
}
