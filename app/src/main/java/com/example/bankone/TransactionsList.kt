package com.example.bankone


data class TransactionsList(
    val data: List<Transaction>?
)

data class Transaction(
    val type : String,
    val amount : Int,
    val phoneNumber : String,
    val created : String
)

data class TransactionResponse(
    val status : Int?,
    val data : User?
)
