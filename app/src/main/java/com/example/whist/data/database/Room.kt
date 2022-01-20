package com.example.whist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.whist.data.database.dao.FcmDao
import com.example.whist.data.database.dao.PartieDao
import com.example.whist.data.database.dao.PlayerDao
import com.example.whist.data.database.dao.TableGameDao
import com.example.whist.data.database.entities.DatabaseFcm
import com.example.whist.data.database.entities.DatabasePartie
import com.example.whist.data.database.entities.DatabasePlayer
import com.example.whist.data.database.entities.DatabaseTableGame


@Database(
    entities = [DatabasePlayer::class, DatabaseTableGame::class, DatabaseFcm::class, DatabasePartie::class],
    version = 1,
    exportSchema = false
)
abstract class WhistDatabase : RoomDatabase() {

    abstract val playerDao: PlayerDao
    abstract val tableGameDao: TableGameDao
    abstract val fcmDao: FcmDao
    abstract val partieDao: PartieDao
}

// For Singleton instantiation
@Volatile
private lateinit var INSTANCE: WhistDatabase
private const val DATABASE_NAME = "whist"

fun getDatabase(context: Context): WhistDatabase {
    synchronized(WhistDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                WhistDatabase::class.java,
                DATABASE_NAME
            )
//                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
