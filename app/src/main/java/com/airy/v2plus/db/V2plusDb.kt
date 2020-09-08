package com.airy.v2plus.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.airy.v2plus.BuildConfig
import com.airy.v2plus.model.official.Node

/**
 * Created by Airy on 2020/7/23
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
@Database(
    entities = [Node::class],
    version = 1,
    exportSchema = false
)
abstract class V2plusDb: RoomDatabase() {

    private val TAG = "V2plusDb"

    companion object {

        @Volatile
        private var INSTANCE: V2plusDb? = null

        fun getDb(context: Context): V2plusDb {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = create(context, false)
                INSTANCE = instance
                return instance
            }
        }

        private fun create(context: Context, inMemory: Boolean): V2plusDb {
            val databaseBuilder = if (inMemory) {
                Room.inMemoryDatabaseBuilder(context, V2plusDb::class.java)
            } else {
                Room.databaseBuilder(context, V2plusDb::class.java, "v2plus.db")
            }
            return databaseBuilder.apply {
                fallbackToDestructiveMigrationFrom()
                if (BuildConfig.DEBUG) {
                    addCallback(DbCallBack())
                }
            }.build()
        }
    }

    abstract fun nodeDao(): NodeDao

    private class DbCallBack : RoomDatabase.Callback() {

        private val TAG = "DbCallBack"

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d(TAG, "onCreate")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d(TAG, "onOpen")
        }

        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Log.d(TAG, "onDestructiveMigration")
        }
    }
}