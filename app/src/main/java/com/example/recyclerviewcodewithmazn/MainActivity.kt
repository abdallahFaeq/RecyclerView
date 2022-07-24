package com.example.recyclerviewcodewithmazn

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter:RecyclerAdapter
    private lateinit var deleteCountry:String
    var countriesList: MutableList<String> = mutableListOf()
    var displayList:MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        recyclerView = findViewById(R.id.recyclerView)
        countriesList.add("Egypt")
        countriesList.add("india")
        countriesList.add("iraq")
        countriesList.add("libia")
        countriesList.add("imarat")
        countriesList.add("qatar")
        countriesList.add("tunos")
        countriesList.add("jazaer")
        countriesList.add("rusia")
        countriesList.add("koorea")
        countriesList.add("china")
        countriesList.add("suodia")
        countriesList.add("qods")

        displayList.addAll(countriesList)
        recyclerAdapter = RecyclerAdapter(displayList)
        recyclerView.adapter = recyclerAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // swipe to refresh layout
        swipeRefreshLayout.setOnRefreshListener {
            displayList.clear()
            displayList.addAll(countriesList)
            recyclerView.adapter?.notifyDataSetChanged()

            swipeRefreshLayout.isRefreshing = false
        }

    }

    // move items in recycler view or change position of itemsList
    private var simpleCallback = object :ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,// start position
            target: RecyclerView.ViewHolder// end position
        ): Boolean {
            val startPosition = viewHolder.adapterPosition
            val endPosition = target.adapterPosition

            Collections.swap(displayList,startPosition,endPosition)
            recyclerView.adapter?.notifyItemMoved(startPosition,endPosition)
            return true

        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition

            when(direction){
                ItemTouchHelper.LEFT -> {
                    deleteCountry = displayList.get(position)
                    displayList.removeAt(position)
                    recyclerAdapter.notifyItemRemoved(position)

                    Snackbar.make(recyclerView,"$deleteCountry is removed",Snackbar.LENGTH_LONG).setAction("undo",object :
                        View.OnClickListener{
                        override fun onClick(view: View?) {
                            displayList.add(position,deleteCountry)
                            recyclerAdapter.notifyItemInserted(position)
                        }
                    }).show()

                }
                ItemTouchHelper.RIGHT ->{
                    var editText = EditText(this@MainActivity)

                    var builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("item updated")
                    builder.setCancelable(true)
                    builder.setView(editText)
                    editText.setText(displayList.get(position))
                    builder.setNegativeButton("cancel",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, p1: Int) {
                            displayList.clear()
                            displayList.addAll(countriesList)
                            recyclerView.adapter?.notifyDataSetChanged()
                        }
                    })
                    builder.setPositiveButton("update",object :DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, p1: Int) {
                            displayList.set(position,editText.text.toString())
                            recyclerView.adapter?.notifyItemChanged(position)
                        }
                    })
                    builder.show()
                }
            }
        }

    }

    // search view
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_item,menu)
        var item = menu!!.findItem(R.id.search_view)

        if (item != null){
            // initialize search view
            var searchView = item.actionView as SearchView

            searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        if (newText.isNotEmpty()){
                            displayList.clear()
                            var search = newText.toLowerCase(Locale.getDefault())

                            for (country in countriesList ){

                                if (country.toLowerCase(Locale.getDefault()) == search){
                                    displayList.add(country)
                                }
                                recyclerView.adapter!!.notifyDataSetChanged()
                            }

                        }else{
                            displayList.clear()
                            displayList.addAll(countriesList)
                            recyclerView.adapter!!.notifyDataSetChanged()
                        }
                    }
                    return false
                }

            })
        }

        return true
    }
}