package com.example.teabreak

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Half.toFloat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlin.String
import kotlin.Float
import com.example.teabreak.DatabaseOperations


class AddTeaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tea)

        val name = findViewById<EditText>(R.id.tea_name)
        val type = findViewById<EditText>(R.id.tea_type)
        val origin = findViewById<EditText>(R.id.tea_origin)
        val amount = findViewById<EditText>(R.id.tea_ammt)
        val temp = findViewById<EditText>(R.id.tea_temp)
        val time = findViewById<EditText>(R.id.tea_time)

        amount.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL)
        time.setRawInputType(InputType.TYPE_CLASS_NUMBER)

        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            if(name.text.toString() == "" ||
                type.text.toString() == "" ||
                origin.text.toString() == "" ||
                amount.text.toString() == "" ||
                temp.text.toString() == "" ||
                time.text.toString() == ""){
                val toast = Toast.makeText(this, "Invalid input: fill all fields", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val dbOps = DatabaseOperations(this)
                dbOps.addTea(this, name.text.toString(), type.text.toString(), origin.text.toString(),
                    amount.text.toString(), (temp.text.toString()).toInt(), (time.text.toString()).toInt())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
