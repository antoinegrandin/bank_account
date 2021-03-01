package com.example.bankaccount

class Account {
    var id = 0
    var accountName: String? = null
    var amount: String? = null
    var iban: String? = null
    var currency: String? = null

    constructor() {}
    constructor(id: Int, accountName: String?, amount: String?, iban: String?, currency: String?) {
        this.id = id
        this.accountName = accountName
        this.amount = amount
        this.iban = iban
        this.currency = currency
    }
}