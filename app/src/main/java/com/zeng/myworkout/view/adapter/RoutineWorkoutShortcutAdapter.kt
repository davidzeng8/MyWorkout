package com.zeng.myworkout.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeng.myworkout.databinding.ListItemRoutineWorkoutShortcutBinding
import com.zeng.myworkout.model.User
import com.zeng.myworkout.model.Workout
import com.zeng.myworkout.viewmodel.RoutineWorkoutShortcutViewModel
import kotlinx.coroutines.launch

class RoutineWorkoutShortcutAdapter(
    private val context: Context,
    private val viewModel: RoutineWorkoutShortcutViewModel
) : ListAdapter<Workout, RecyclerView.ViewHolder>(WorkoutDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RoutineWorkoutShortcutViewHolder(ListItemRoutineWorkoutShortcutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as RoutineWorkoutShortcutViewHolder).bind(item)
    }

    inner class RoutineWorkoutShortcutViewHolder(private val binding: ListItemRoutineWorkoutShortcutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Workout) {
            binding.apply {
                workout = item

                button.setOnClickListener {
                    viewModel.viewModelScope.launch {
                        val user = viewModel.getUser()
                        // Check if the user currently has a session
                        if (user?.workoutSessionId != null) {
                            openValidationDialog(user, item)
                        } else {
                            setUserWorkout(item)
                        }
                    }
                }
            }
        }

        private fun openValidationDialog(user: User, workout: Workout) {
            val dialog = AlertDialog.Builder(context)
                .setMessage("Close current workout session ?")
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteWorkout(user.workoutSessionId!!)
                    setUserWorkout(workout)
                }
                .setNegativeButton("CANCEL") {  _, _ ->  }
                .create()

            dialog.show()
        }

        private fun setUserWorkout(workout: Workout) {
            viewModel.viewModelScope.launch {
                viewModel.updateUserWorkout(workout)
                // TODO FIX
//                val navController = (context as FragmentActivity).findNavController(R.id.nav_host_fragment)
//                navController.navigate(R.id.action_navigation_routine_to_navigation_workout)
            }
        }
    }

}

class WorkoutDiffCallback : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }

}
