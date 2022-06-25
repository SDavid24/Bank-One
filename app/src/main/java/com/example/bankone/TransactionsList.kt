package com.example.bankone


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

data class TransactionResponse(
    var status : Int?,
    var data : User?
)
