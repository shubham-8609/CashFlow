package com.codeleg.cashflow.ui.fragment


interface NavigationListener {
    fun navigateToAddExpense()
    fun navigateToHome()
    fun navigateToEditExpense(expenseId: Int)
}
