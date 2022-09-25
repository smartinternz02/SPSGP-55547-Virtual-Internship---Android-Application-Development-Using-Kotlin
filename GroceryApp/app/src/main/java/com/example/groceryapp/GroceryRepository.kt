package com.example.groceryapp

class GroceryRepository(private val db:GroceryDatabase) {
    suspend fun insert(items: GroceryItems) = db.getGrocerDao().insert(items)
    suspend fun delete(items: GroceryItems) = db.getGrocerDao().delete(items)

    fun getAllItems() = db.getGrocerDao().getAllGroceryItems()

}