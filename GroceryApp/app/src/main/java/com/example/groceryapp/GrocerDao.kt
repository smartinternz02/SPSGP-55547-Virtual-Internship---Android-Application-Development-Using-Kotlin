package com.example.groceryapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GrocerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: GroceryItems)

    @Delete
    fun delete(item: GroceryItems)

    @Query(value= "SELECT * FROM grocerry_items")
    fun getAllGroceryItems() : LiveData<List<GroceryItems>>

}