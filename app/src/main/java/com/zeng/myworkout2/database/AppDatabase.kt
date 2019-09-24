package com.zeng.myworkout2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.zeng.myworkout2.model.*
import com.zeng.myworkout2.util.DataConverter
import com.zeng.myworkout2.worker.DatabaseWorker

@Database(
    entities = [
        UserSql::class,
        RoutineSql::class,
        WorkoutSql::class,
        WorkoutExerciseSql::class,
        Exercise::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun routineDao(): RoutineDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): WorkoutExerciseDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            context.deleteDatabase("my_workout_database")
            return Room.databaseBuilder(context, AppDatabase::class.java, "my_workout_database")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<DatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }

    }
}