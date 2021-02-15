package com.example.teabreak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*


/**
 * AddTeaActivity:
 *
 * @desc Activity that handles the user input for adding a tea to the SQLite database
 */

class AddTeaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tea)
        title = "Add a Tea"

        // Retrieving references to the edit fields in the activity
        val name = findViewById<EditText>(R.id.tea_name)
        val type = findViewById<Spinner>(R.id.tea_type)
        val origin = findViewById<EditText>(R.id.tea_origin)
        val amount = findViewById<EditText>(R.id.tea_ammt)
        val temp = findViewById<EditText>(R.id.tea_temp)
        val time = findViewById<EditText>(R.id.tea_time)
        val measurement = findViewById<Spinner>(R.id.measurement)

        amount.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
        time.setRawInputType(InputType.TYPE_CLASS_NUMBER)


        /* Setting spinner logic */

        // Tea Type
        val teaTypes = resources.getStringArray(R.array.TeaTypes)
        var selectedType = teaTypes[0]
        if (type != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, teaTypes)
            type.adapter = adapter

            type.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // Not implemented
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    selectedType = teaTypes[position]
                }
            }
        }
        // Measurement
        val measurements = resources.getStringArray(R.array.Measurements)
        var selectedMeasurement = measurements[0]
        if (measurement != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, measurements)
            measurement.adapter = adapter

            measurement.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    // Not implemented
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    selectedMeasurement = measurements[position]
                }
            }
        }

        // TODO: Add image selection functionality

        /* Binding add button */

        val button = findViewById<Button>(R.id.add_button)
        button.setOnClickListener {
            if(name.text.toString() == "" ||
                origin.text.toString() == "" ||
                amount.text.toString() == "" ||
                temp.text.toString() == "" ||
                time.text.toString() == "") {
                val toast = Toast.makeText(this, "Invalid input: fill all fields", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val dbOps = DatabaseOperations(this)
                dbOps.addTea(this,
                    name.text.toString(),
                    selectedType,
                    amount.text.toString() + " " + selectedMeasurement,
                    (temp.text.toString()).toInt(),
                    (time.text.toString()).toInt())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
