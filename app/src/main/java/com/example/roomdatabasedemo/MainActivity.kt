package com.example.roomdatabasedemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roomdatabasedemo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class   MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appDB: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDB = AppDatabase.getDatabase(this)

        binding.btnWriteData.setOnClickListener {
            writeData()
        }
        binding.btnReadData.setOnClickListener {
            readData()
        }

        binding.btnDeleteAll.setOnClickListener {
            GlobalScope.launch {
                appDB.studentDao().deleteAll()
            }
        }
        binding.btnUpdateData.setOnClickListener {
            updateData()
        }

    }

    private fun updateData(){

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                appDB.studentDao().update(firstName, lastName, rollNo.toInt())
            }
            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()
            Toast.makeText(this, "Successfully updated ", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun displayData(student: Student){
        withContext(Dispatchers.Main){
                binding.tvFirstName.text = student.firstName
                binding.tvLastName.text = student.lastName
                binding.tvRollNo.text = student.rollNo.toString()
        }
    }

    private fun readData() {

        val rollNo = binding.etRollNo.text.toString()
        if (rollNo.isNotEmpty()) {
            lateinit var student: Student
            GlobalScope.launch(Dispatchers.IO) {
                student = appDB.studentDao().findByRoll(rollNo.toInt())
                displayData(student)
            }

        }
    }


    private fun writeData() {

        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            val student = Student(null, firstName, lastName, rollNo.toInt())

            GlobalScope.launch(Dispatchers.IO) {
                appDB.studentDao().insert(student)
            }
            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()
            Toast.makeText(this, "Successfully written ", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Please enter data", Toast.LENGTH_SHORT).show()
        }

    }

}