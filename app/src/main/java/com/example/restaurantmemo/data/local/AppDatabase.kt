package com.example.restaurantmemo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [RestaurantEntity::class, RestaurantTagEntity::class, TagEntity::class, VisitEntity::class],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "restaurant_memo.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build()
                    .also { INSTANCE = it }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE visits ADD COLUMN photoUri TEXT")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS restaurant_tags (
                        restaurantId INTEGER NOT NULL,
                        name TEXT NOT NULL,
                        PRIMARY KEY(restaurantId, name),
                        FOREIGN KEY(restaurantId) REFERENCES restaurants(id) ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE INDEX IF NOT EXISTS index_restaurant_tags_restaurantId ON restaurant_tags(restaurantId)")
                db.execSQL("CREATE INDEX IF NOT EXISTS index_restaurant_tags_name ON restaurant_tags(name)")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE restaurants ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS tags (
                        name TEXT NOT NULL,
                        PRIMARY KEY(name)
                    )
                    """.trimIndent()
                )
                listOf("ラーメン", "喫茶店", "居酒屋", "駅近", "一人向き").forEach { tag ->
                    db.execSQL("INSERT OR IGNORE INTO tags(name) VALUES(?)", arrayOf(tag))
                }
                db.execSQL(
                    """
                    INSERT OR IGNORE INTO tags(name)
                    SELECT DISTINCT name FROM restaurant_tags
                    WHERE TRIM(name) != ''
                    """.trimIndent()
                )
            }
        }
    }
}
