package com.fahmicode.data

import com.fahmicode.data.dao.TransactionDao
import com.fahmicode.data.dao.BudgetDao
import com.fahmicode.data.model.Transaction
import com.fahmicode.data.model.Budget
import com.fahmicode.data.network.CurrencyApiService
import com.fahmicode.data.network.ExchangeRateResponse
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val currencyApiService: CurrencyApiService = CurrencyApiService.create()
) {
    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()
    val allBudgets: Flow<List<Budget>> = budgetDao.getAllBudgets()

    suspend fun getExchangeRates(): ExchangeRateResponse {
        return currencyApiService.getUsdExchangeRates()
    }

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.deleteTransaction(transaction)
    }

    suspend fun insertBudget(budget: Budget) {
        budgetDao.insertBudget(budget)
    }

    suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }

    suspend fun clearAllData() {
        transactionDao.clearAllTransactions()
        budgetDao.clearAllBudgets()
    }
}
