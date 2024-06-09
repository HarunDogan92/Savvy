package com.example.savvy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.savvy.entities.Budget
import com.example.savvy.entities.Income
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [Budget::class, Income::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SavvyDatabase: RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun incomeDao(): IncomeDao

    companion object{
        @Volatile
        private var instance: SavvyDatabase? = null

        fun getDatabase(context: Context, coroutineScope: CoroutineScope): SavvyDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, SavvyDatabase::class.java, "savvy_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback = DatabaseCallback(coroutineScope))
                    .build()
                    .also {
                        instance = it
                    }
            }
        }
        private class DatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { database ->
                    scope.launch {
                        val dao = database.budgetDao()
                    }
                }
            }

        }
    }

}

