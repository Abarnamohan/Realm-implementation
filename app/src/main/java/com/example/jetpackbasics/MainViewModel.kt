package com.example.jetpackbasics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackbasics.models.Address
import com.example.jetpackbasics.models.Course
import com.example.jetpackbasics.models.Student
import com.example.jetpackbasics.models.Teacher
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val realm = MyApp.realm

    val courses =realm.query<Course>().asFlow().map {results->
        results.list.toList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        createSampleEntries()
    }
    private fun createSampleEntries(){

        viewModelScope.launch {

            realm.write {

                var address1 = Address().apply {

                    fullName =" Abarna"
                    street= "street1"
                    houseNumber = 24
                    zip = 54321
                    city ="city1"
                }
                var address2 = Address().apply {

                    fullName =" Aarthi"
                    street= "street2"
                    houseNumber = 242
                    zip = 54322
                    city ="city2"
                }

                val course1 = Course().apply {
                    name= "Java"
                }
                val course2 = Course().apply {
                    name= "Kotlin"
                }
                val course3 = Course().apply {
                    name= "Jetpack"
                }
                val course4 = Course().apply {
                    name= "Flutter"
                }

                val teacher1 = Teacher().apply{
                    address = address1
                    courses = realmListOf(course1,course2)
                }

                val teacher2 = Teacher().apply{
                    address = address2
                    courses = realmListOf(course3,course4)
                }

                course1.teacher = teacher1
                course2.teacher = teacher1
                course3.teacher = teacher2
                course4.teacher = teacher2

                address1.teacher = teacher1
                address2.teacher = teacher2

                val student1 = Student().apply {
                    name = "John"
                }
                val student2 = Student().apply {
                    name = "Sam"
                }

                course1.enrolledStudents.add(student1)
                course2.enrolledStudents.add(student2)
                course3.enrolledStudents.addAll(listOf(student1,student2))

                copyToRealm(teacher1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(teacher2, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(course1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course3, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(course4, updatePolicy = UpdatePolicy.ALL)

                copyToRealm(student1, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(student2, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

}