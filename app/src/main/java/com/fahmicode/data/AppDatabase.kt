package com.fahmicode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fahmicode.data.dao.TransactionDao
import com.fahmicode.data.dao.BudgetDao
import com.fahmicode.data.model.Transaction
import com.fahmicode.data.model.Budget

@Database(entities = [Transaction::class, Budget::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetpecker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
