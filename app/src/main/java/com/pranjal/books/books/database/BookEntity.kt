package com.pranjal.books.books.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="books")

 data class BookEntity (
    @PrimaryKey val book_id:Int,
    @ColumnInfo val bookName:String,
    @ColumnInfo val bookAuthor:String,
    @ColumnInfo val bookPrice:String,
    @ColumnInfo val bookRating:String,
    @ColumnInfo val content:String,
    @ColumnInfo val bookImage:String


    )

