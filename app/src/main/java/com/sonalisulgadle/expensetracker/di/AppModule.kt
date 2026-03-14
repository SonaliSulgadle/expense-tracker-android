package com.sonalisulgadle.expensetracker.di

import android.content.Context
import androidx.room.Room
import com.sonalisulgadle.expensetracker.data.local.ExpenseDao
import com.sonalisulgadle.expensetracker.data.local.ExpenseDatabase
import com.sonalisulgadle.expensetracker.data.repository.ExpenseRepositoryImpl
import com.sonalisulgadle.expensetracker.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase =
        Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        ).build()

    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao =
        database.expenseDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Tells Hilt: when someone asks for ExpenseRepository, give them ExpenseRepositoryImpl
    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        impl: ExpenseRepositoryImpl
    ): ExpenseRepository
}