package com.example.alleassignment.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.alleassignment.data.dao.ImageDao
import com.example.alleassignment.data.entity.ImageEntity

@Database(entities = [ImageEntity::class], version = 1)
@TypeConverters(ImageConverters::class)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var INSTANCE: ImagesDatabase? = null

        fun getDatabase(context: Context): ImagesDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ImagesDatabase::class.java,
                            "images_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}