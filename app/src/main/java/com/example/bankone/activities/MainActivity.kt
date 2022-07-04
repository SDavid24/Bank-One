package com.example.bankone.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bankone.R
import com.example.bankone.adapters.TransactionsAdapter
import com.example.bankone.models.*
import com.example.bankone.mvvm.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_progress.*
import kotlinx.android.synthetic.main.dropdown_text_view.*
import kotlinx.android.synthetic.main.partial_main_activity.*
import kotlinx.android.synthetic.main.partial_payment_activity.*
import kotlinx.android.synthetic.main.partial_transact_activity.*

class MainActivity : AppCompatActivity() {
    private lateinit var transactionsAdapter: TransactionsAdapter
    private lateinit var homeTransAdapter: TransactionsAdapter
    private lateinit var viewModel :  MainViewModel
    private lateinit var newList : MutableList<Transaction>

    //private lateinit var withdrawal : Withdrawal


    /**This is a progress dialog instance which we will initialize later on.*/
    private lateinit var mProgressDialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val user : User?
        if (intent.hasExtra(SignInActivity.ACCOUNT_DETAILS)){
            user = intent.getSerializableExtra(
                SignInActivity.ACCOUNT_DETAILS
            ) as User

            main_account_number.text = user.phoneNumber
            payment_textView4.text = user.phoneNumber
        }

        send_button.setOnClickListener {
           // if(amount_to_transfer.text.isNotEmpty()){
            if(validateTransferForm(transfer_to_who_text.text.toString(),
                    amount_to_transfer.text.toString())){
                moneyTransfer()
            }
        }

        withdraw_button.setOnClickListener {
            if(validateWithdrawalForm(withdraw_from_who_text.text.toString(),
                    amount_to_withdraw.text.toString())){
                moneyWithdraw()
            }
        }

        getTransactList()
        homeRecyclerView()
        transactionsRecyclerView()

        /**Click listener for the Transaction icon*/
        newTransaction.setOnClickListener {
            ll_home_view.visibility = View.GONE
            ll_payment_view.visibility = View.GONE
            ll_transaction_view.visibility = View.VISIBLE
            getTransactList()
        }

        /**Click listener for the home icon*/
        newHomeBtn.setOnClickListener {
            ll_transaction_view.visibility = View.GONE
            ///history_view.visibility = View.GONE
            ll_payment_view.visibility = View.GONE
            ll_home_view.visibility = View.VISIBLE
            getTransactList()
        }

        /**Click listener for the Payment button*/
        payment_button.setOnClickListener {
            ll_transaction_view.visibility = View.GONE
            ll_home_view.visibility = View.GONE
            ll_payment_view.visibility = View.VISIBLE
        }

        transactionTypeDropdown()

        /**Click listener for the sign out icon. It takes the user back to the login page*/
        signoutMenu.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }


    /**Method that calls in the method in the view model that's is in charge of the business logic behind transferring or depositing money from an indicated account using the API */
    fun moneyTransfer(){
        val transferDestination = transfer_to_who_text.text.toString()
        val amount = amount_to_transfer.text.toString()
        showProgressDialog("Please wait...")

        val transfer = Transfer(transferDestination, amount.toInt())
        viewModel.transferMoneyObservable().observe(this,
            Observer<TransactionResponse?> { response ->

                if (response == null) {
                    hideProgressDialog()
                    Toast.makeText(this@MainActivity, "Transfer failed!. Check the input details or your data connection and try again", Toast.LENGTH_LONG).show()
                } else {
                    hideProgressDialog()
                    Toast.makeText(this@MainActivity, "Transfer successful!...",
                        Toast.LENGTH_LONG).show()
                    transfer_to_who_text.text.clear()
                    amount_to_transfer.text.clear()
                }

            })

        viewModel.transferMoney(transfer)
    }

    /**Method that calls in the method in the view model that's is in charge of the business logic behind withdrawing money from an indicated account using the API */
    private fun moneyWithdraw(){
        val withdrawalAccount = withdraw_from_who_text.text.toString()
        val amount = amount_to_withdraw.text.toString()
        showProgressDialog("Please wait...")

        viewModel.withdrawMoneyObservable().observe(this,
            Observer<TransactionResponse?> { response ->

                if (response == null) {
                    hideProgressDialog()
                    Toast.makeText(this@MainActivity, "Transfer failed!. Check the input details or your data connection and try again.", Toast.LENGTH_LONG).show()
                } else {
                    hideProgressDialog()
                    Toast.makeText(this@MainActivity, "Withdrawal successful!", Toast.LENGTH_LONG).show()
                    withdraw_from_who_text.text.clear()
                    amount_to_withdraw.text.clear()
                }

            })

        val withdrawal = Withdrawal(withdrawalAccount, amount.toInt())
        viewModel.withdrawMoney(withdrawal)
    }


    /**An external check that is done to see if any of the text views in the Transfer details form is blank when she tries to carry out a Transfer transaction and then notifies the user immediately. */
    private fun validateTransferForm(TransferTo: String,
                                   amount: String) : Boolean{
        return when {
            TextUtils.isEmpty(TransferTo) ->{
                showErrorSnackBar("Please enter an account number to transfer to")
                false
            }

            TextUtils.isEmpty(amount) ->{
                showErrorSnackBar("Amount to be transferred is not stated")
                false
            }

            else -> {
                true
            }
        }
    }

    /**An external check that is done to see if any of the text views in the WithdrawaL details form is blank when she tries to carry out a Withdrawal transaction and then notifies the user immediately. */
    private fun validateWithdrawalForm(withdrawTo: String,
                                   amount: String) : Boolean{
        return when {
            TextUtils.isEmpty(withdrawTo) ->{
                showErrorSnackBar("Please enter an account number to withdraw from")
                false
            }
            TextUtils.isEmpty(amount) ->{
                showErrorSnackBar("Amount to be withdrawn is not stated")
                false
            }

            else -> {
                true
            }
        }
    }


    /**Method that configures the dropdown menu which indicates the type of transaction that's being carried out. Either Transfer or Withdraw*/
    private fun transactionTypeDropdown(){

        val transactionType = resources.getStringArray(R.array.Transaction_type)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_text_view, transactionType)
        autoCompleteTextView.setAdapter(arrayAdapter)


        autoCompleteTextView.setOnItemClickListener( object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(adapterView: AdapterView<*>?,
                                        p1: View?, position: Int, p3: Long) {
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            /**What should happen when a particular click listener is selected*/
            override fun onItemClick(adapterView: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                when {
                    autoCompleteTextView.text.toString() == "Transfer/Deposit" -> {
                        //ll_deposit_form.visibility = View.GONE
                        amount_to_withdraw.text.clear()
                        withdraw_from_who_text.text.clear()
                        ll_withdraw_form.visibility = View.GONE
                        ll_transfer_form.visibility = View.VISIBLE
                    }
                    autoCompleteTextView.text.toString() == "Withdrawal" -> {
                        //ll_deposit_form.visibility = View.GONE
                        amount_to_transfer.text.clear()
                        transfer_to_who_text.text.clear()

                        ll_transfer_form.visibility = View.GONE
                        ll_withdraw_form.visibility = View.VISIBLE
                    }
                    else -> {
                        Toast.makeText(
                            this@MainActivity, "You did not select anything",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
        })
    }


    /**Method that calls in the method in the view model that's is in charge of the business logic behind getting the list of all transactions from the API source*/
    fun getTransactList(){
        showProgressDialog("Please wait...")
        viewModel.transactionListObservable().observe(this,
            Observer<TransactionsList?> { response ->

                if (response == null) {
                    hideProgressDialog()
                    Toast.makeText(this@MainActivity, "no result found...", Toast.LENGTH_LONG).show()

                } else {
                    hideProgressDialog()

                    homeTransAdapter.list = response.data!!.toMutableList()
                    transactionsAdapter.list = response.data!!.toMutableList().takeLast(5) as MutableList<Transaction>

                    transactionsAdapter.notifyDataSetChanged()
                }

            })

        viewModel.transactionsLists()
    }



    /**Method to load the recycler view transaction list data in the home view*/
    private fun homeRecyclerView() {
        homeTransAdapter = TransactionsAdapter(this)
        all_transactions.adapter = homeTransAdapter

        //rv_last_transactions.layoutManager = LinearLayoutManager(this)
        all_transactions.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        all_transactions.setHasFixedSize(true)
    }

    /**Method to load the recycler view transaction list data in the All transactions view*/
    private fun transactionsRecyclerView() {
        transactionsAdapter = TransactionsAdapter(this)

        rv_last_transactions.adapter = transactionsAdapter
        rv_last_transactions.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        rv_last_transactions.setHasFixedSize(true)
    }


    /**Method to show the circling progress dialog when something is loading*/
    fun showProgressDialog(text: String){

        /*Set the screen content from a layout resource
        The resource will be inflated, adding all top-level views to the screen
         */
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)  //Setting the circling progress icon
        mProgressDialog.tv_progress_text.text = text   //Setting the text

        //Starts the dialog and displays is on the screen
        mProgressDialog.show()
    }

    /**Method to dismiss dialog*/
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    /**Method for the snack bar that'll display throughout the app*/
    fun showErrorSnackBar(message: String) {
        val snackBar = Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        )
        val snackBarView = snackBar.view

        snackBarView.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.snackBar_error_color
            )
        )  //To set background color of snack bar

        snackBar.show()
    }


}