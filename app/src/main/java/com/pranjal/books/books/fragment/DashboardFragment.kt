package com.pranjal.books.books.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pranjal.books.R
import com.pranjal.books.books.adapter.DashboardRecyclerAdapter
import com.pranjal.books.books.model.Book
import com.pranjal.books.books.util.ConnectionManager
import org.json.JSONException
import java.util.ArrayList


class DashboardFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var dashboardLayoutManager: RecyclerView.LayoutManager

    lateinit var progress:ProgressBar
//lateinit var bookInfoList:ArrayList<Book>
  val bookInfoList = arrayListOf<Book>(
        Book("Life Of Pie",
            "€299",
            "Yann Martel",
            "4.9",
        "fhgfhg",
        "ghfhgf"))/*
        ,
        Book(
            "Harry Potter and the Sorcerer's stone",
            "€560",
            "J.K.Rowling",
            "5.0",

        )
        ,
        Book(
            "Harry Potter and the Chamber Of Secrets",
            "€438",
            "J.K.Rowling",
            "3.9",

        )
        ,
        Book("Harry Potter and the Goblet Of Fire", "€345", "J.K.Rowling", "4.0", "R.drawable.goblet")
        ,
        Book(
            "Harry Potter and The Prisoner Of Azkaban",
            "€455",
            "J.K.Rowling",
            "4.6",

        )
        ,
        Book(
            "Harry Potter and the Order Of The Phoenix",
            "€689",
            "J.K.Rowling",
            "5.0",

        )
        ,
        Book(
            "Harry Potter and The Half Blood Prince ",
            "€560",
            "J.K.Rowling",
            "4.0",
           "R.drawable.half1"
        "hgjhgjh"),
        Book(
            "Harry Potter and the Deathly Hallows Part-1",
            "€344",
            "J.K.Rowling",
            "5.0",
            "R.drawable.deathly1"
        ),
        Book(
            "Harry Potter and the Deathly Hallows PART -two",
            "€600",
            "J.K.Rowling",
            "5.0",
            "R.drawable.deathly2"
        ),
        Book("Harry Potter and the Cursed Child",
            "€400",
            "J.K.Rowling",
            "5.0",
            "R.drawable.stoone")*/




    lateinit var dashboardAdaptor: DashboardRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        recyclerView = view.findViewById(R.id.recycleView)
        dashboardLayoutManager = LinearLayoutManager(activity)

        progress=view.findViewById(R.id.progress_circular)
        progress.visibility=View.VISIBLE



        val queue = Volley.newRequestQueue(activity as Context)
        val url ="http://13.235.250.119/v1/book/fetch_books"
        if(ConnectionManager().checkConnectivity(activity as Context )){
val jsonObjectRequest= object: JsonObjectRequest( Request.Method.GET,url,null, Response.Listener{
    println("Response is $it")
    try {
        progress.visibility=View.GONE
        val success = it.getBoolean("success")
        if (success) {
            val data = it.getJSONArray("data")
            for (i in 0 until data.length()) {
                val bookJsonObject = data.getJSONObject(i)
                val bookObject = Book(
                    bookJsonObject.getString("book_id"),
                    bookJsonObject.getString("name"),
                    bookJsonObject.getString("author"),
                    bookJsonObject.getString("rating"),
                    bookJsonObject.getString("price"),
                    bookJsonObject.getString("image")
                )

                bookInfoList.add(i, bookObject)
            }
            dashboardAdaptor = DashboardRecyclerAdapter(activity as Context, bookInfoList)
            recyclerView.adapter = dashboardAdaptor
            recyclerView.layoutManager = dashboardLayoutManager as LinearLayoutManager


        }

    else
        Toast.makeText(activity as Context," Some error occured",Toast.LENGTH_LONG).show()}
    catch (e:JSONException){
        Toast.makeText(activity,"Unexpected error occured,try again later",Toast.LENGTH_LONG)
    }
},Response.ErrorListener{
    Toast.makeText(activity,"Volley error!!!",Toast.LENGTH_LONG)
})
{
    override fun getHeaders():MutableMap<String,String>{
        val headers =HashMap<String,String>()
        headers["Content-type"]="application/json"
        headers["token"]="56030a473bfee6"
        return headers
    }
}
        queue.add(jsonObjectRequest)}
        else
        {
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("ERROR")
            dialog.setMessage(" No Internet Connection Found")
            dialog.setNegativeButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setPositiveButton("Exit")
            { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            // INTERNET
            dialog.create()
            dialog.show()
        }

        return view


    }
}
