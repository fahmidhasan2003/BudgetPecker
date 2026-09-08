package com.fahmicode.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fahmicode.data.dao.TransactionDao
import com.fahmicode.data.dao.BudgetDao
import com.fahmicode.data.dao.NoteDao
import com.fahmicode.data.model.Transaction
import com.fahmicode.data.model.Budget
import com.fahmicode.data.model.Note

@Database(entities = [Transaction::class, Budget::class, Note::class], version = 4, exportSchema = false)
@TypeConverters(NoteTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `notes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `checklistItems` TEXT NOT NULL, `amount` REAL, `expenseCategory` TEXT, `dateMillis` INTEGER NOT NULL, `colorHex` INTEGER NOT NULL)"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE budgets ADD COLUMN month INTEGER NOT NULL DEFAULT -1")
                database.execSQL("ALTER TABLE budgets ADD COLUMN year INTEGER NOT NULL DEFAULT -1")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budgetpecker_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_3_4)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
