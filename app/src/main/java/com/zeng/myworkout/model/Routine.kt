package com.zeng.myworkout.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity(tableName = "routine")
data class Routine(
    @ColumnInfo
    var name: String = "",

    @ColumnInfo
    var description: String = "",

    @ColumnInfo
    var order: Int = 0,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)

data class RoutineWithWorkouts(
    @Embedded
    var routine: Routine = Routine()
) {
    @Ignore
    lateinit var workoutsLiveData: LiveData<List<Workout>>
}