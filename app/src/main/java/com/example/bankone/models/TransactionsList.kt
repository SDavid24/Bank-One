package com.example.bankone.models


data class TransactionsList(
    var data: List<Transaction>?
)

data class Transaction(
    var type : String,
    var balance : Int,
    var amount : Int,
    var phoneNumber : String,
    var created : String
)

data class Transfer(
    var phoneNumber : String,
    var amount : Int,
    )


data class Withdrawal(
    var phoneNumber : String,
    var amount : Int,
    )


data class TransactionResponse(
    var status : String,
    val message: String?,
    var data : Transfer?
)
