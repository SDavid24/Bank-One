package com.example.bankone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_main_activity.*
import kotlinx.android.synthetic.main.partial_main_activity.rv_last_transactions
import kotlinx.android.synthetic.main.partial_transact_activity.*

class MainActivity : AppCompatActivity() {
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        getTransactList()
        transactionsRecyclerView()

        newTransfer.setOnClickListener {
            home_view.visibility = View.GONE
            history_view.visibility = View.GONE
            transaction_view.visibility = View.VISIBLE

            Toast.makeText(
                this, "The transfer button is clicked", Toast.LENGTH_SHORT
            ).show()

        }

        newHomeBtn.setOnClickListener {
            transaction_view.visibility = View.GONE
            history_view.visibility = View.GONE
            home_view.visibility = View.VISIBLE

            Toast.makeText(
                this, "The home button is clicked", Toast.LENGTH_SHORT
            ).show()
        }

        newHistoryBtn.setOnClickListener {
            transaction_view.visibility = View.GONE
            home_view.visibility = View.GONE
            history_view.visibility = View.VISIBLE
        }
    }

    /**Method that configures the popup icon that's embedded in every recyclerview*/

/*
    fun popupMenus() {
        val popupMenus = PopupMenu(this, view)
        popupMenus.inflate(R.menu.bottom_nav)
        popupMenus.setOnMenuItemClickListener {

            when (it.itemId) {
                //What happens when Delete is clicked on
                R.id.menuHome -> {

                    transaction_view.visibility = View.GONE
                    home_view.visibility = View.VISIBLE

                    Toast.makeText(
                        this, "The home button is clicked", Toast.LENGTH_SHORT
                    ).show()
                }

                R.id.menuTransfer -> {
                    transaction_view.visibility = View.GONE
                    home_view.visibility = View.VISIBLE

                    Toast.makeText(
                        this, "The transfer button is clicked", Toast.LENGTH_SHORT
                    ).show()
                }
            }
            false
        }

        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenus)
        menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
            .invoke(menu,true)
    }
*/


    fun getTransactList(){
        viewModel.transactionListObservable().observe(this,
            Observer<TransactionsList?> { response ->

                if (response == null) {
                    Toast.makeText(this@MainActivity, "no result found...", Toast.LENGTH_LONG)
                        .show()
                } else {
                    transactionsAdapter.list = response.data!!.toMutableList()
                    transactionsAdapter.notifyDataSetChanged()
                }

            })

        viewModel.transactionsLists()
    }

    fun transactionsRecyclerView(){
        transactionsAdapter = TransactionsAdapter(this)

        rv_last_transactions.layoutManager = LinearLayoutManager(this)
        rv_last_transactions.adapter = transactionsAdapter
        rv_last_transactions.setHasFixedSize(true)

        all_transactions.layoutManager = LinearLayoutManager(this)
        all_transactions.adapter = transactionsAdapter
        all_transactions.setHasFixedSize(true)

    }
}