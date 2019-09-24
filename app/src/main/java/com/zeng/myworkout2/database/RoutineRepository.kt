package com.zeng.myworkout2.database

import androidx.lifecycle.LiveData
import com.zeng.myworkout2.model.Routine
import com.zeng.myworkout2.model.RoutineSql

class RoutineRepository private constructor(private val routineDao: RoutineDao) {

    fun getRoutines(): LiveData<List<Routine>> = routineDao.getAllRoutine()
    fun getRoutineSqlById(routineId: Long): LiveData<RoutineSql> = routineDao.getRoutineSqlById(routineId)

    suspend fun insert(routine: RoutineSql): Long = routineDao.insert(routine)
    suspend fun delete(routine: RoutineSql) = routineDao.delete(routine)
    suspend fun update(routines: List<RoutineSql>) = routineDao.update(routines)
    suspend fun upsert(routines: List<RoutineSql>) = routineDao.upsert(routines)

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: RoutineRepository? = null

        fun getInstance(routineDao: RoutineDao) =
            instance ?: synchronized(this) {
                instance ?: RoutineRepository(routineDao).also { instance = it }
            }
    }
}
