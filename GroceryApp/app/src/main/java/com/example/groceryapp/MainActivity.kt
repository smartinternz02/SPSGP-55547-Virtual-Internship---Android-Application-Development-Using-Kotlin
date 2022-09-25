package com.example.groceryapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface {
    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var  groceryViewModal: GroceryViewModal


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        list = ArrayList<GroceryItems>()
        groceryRVAdapter=GroceryRVAdapter(list,this)
        itemsRV.layoutManager=LinearLayoutManager(this)
        itemsRV.adapter=groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModalFactory(groceryRepository)
        groceryViewModal = ViewModelProvider(this,factory).get(GroceryViewModal::class.java)
        groceryViewModal.getAllGroceryItems().observe(this, Observer{
            groceryRVAdapter.list = it
            groceryRVAdapter.notifyDataSetChanged()
        })

        addFAB.setOnClickListener{
            openDialog()
        }
    }
    fun openDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.grocery_add_dialog)
        val cancelBtn = dialog.findViewById<Button>(R.id.idBtnCancel)
        val addBtn = dialog.findViewById<Button>(R.id.idBtnAdd)
        val itemEdit = dialog.findViewById<EditText>(R.id.idEditItemName)
        val itemPriceEdit = dialog.findViewById<EditText>(R.id.idEditItemPrice)
        val itemQuantityEdit = dialog.findViewById<EditText>(R.id.idEditItemQuantity)
        cancelBtn.setOnClickListener{
            dialog.dismiss()
        }
        addBtn.setOnClickListener{
            val itemName : String = itemEdit.text.toString()
            val itemPrice : String = itemPriceEdit.text.toString()
            val itemQuantity : String = itemQuantityEdit.text.toString()
            val qty : Int = itemQuantity.toInt()
            val pr : Int = itemPrice.toInt()
            if(itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty()){
                val items = GroceryItems(itemName,qty,pr)
                groceryViewModal.insert(items)
                Toast.makeText(applicationContext,"Item Inserted..",Toast.LENGTH_SHORT).show()
                groceryRVAdapter.notifyDataSetChanged()
                dialog.dismiss()

            }else{
                Toast.makeText(applicationContext,"please Enter All the Data..",Toast.LENGTH_SHORT).show()
            }


        }

        dialog.show()
    }

    override fun onItemClick(groceryItems: GroceryItems) {
        groceryViewModal.delete(groceryItems)
        groceryRVAdapter.notifyDataSetChanged()
        Toast.makeText(applicationContext,"Item Deleted..",Toast.LENGTH_SHORT).show()
    }
}