package com.kt.kt_web.util

import com.kt.kt_web.entities.model.State
import com.kt.kt_web.entities.model.StateOption
import com.kt.kt_web.entities.model.Task
import java.time.LocalDateTime

class TaskPool {
    companion object {

        fun getRandom(): Task {
            return getTasks().random()
        }

        fun getTasks(): List<Task> {
            val task1 = Task(
                title = "Catering Setup",
                description = "Setting up the catering equipment for the event",
                //stateRecord = mutableListOf()
            )

            val state1 = State(LocalDateTime.now(), null, StateOption.TO_DO, task1)
            //task1.addState(state1)

            val task2 = Task(
                title = "Menu Planning",
                description = "Planning the menu for the upcoming event",
                //stateRecord = mutableListOf()
            )

            val state2 = State(LocalDateTime.now(), null, StateOption.TO_DO, task2)
            //task2.addState(state2)

            val task3 = Task(
                title = "Ingredient Sourcing",
                description = "Sourcing fresh ingredients from local markets",
                //stateRecord = mutableListOf()
            )

            val state3 = State(LocalDateTime.now(), null, StateOption.TO_DO, task3)
            //task3.addState(state3)

            val task4 = Task(
                title = "Table Arrangement",
                description = "Arranging the tables and chairs for the dining area",
                //stateRecord = mutableListOf()
            )

            val state4 = State(LocalDateTime.now(), null, StateOption.TO_DO, task4)
            //task4.addState(state4)

            val task5 = Task(
                title = "Food Preparation",
                description = "Preparing the dishes as per the planned menu",
                //stateRecord = mutableListOf()
            )

            val state5 = State(LocalDateTime.now(), null, StateOption.TO_DO, task5)
            //task5.addState(state5)

            return listOf(task1, task2, task3, task4, task5)
        }
    }
}