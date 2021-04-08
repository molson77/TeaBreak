package com.example.teabreak

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.provider.MediaStore
import android.widget.*
import java.io.ByteArrayOutputStream


/**
 * AddTeaActivity:
 *
 * @desc Activity that handles the user input for adding a tea to the SQLite database
 */

class AddTeaActivity : AppCompatActivity() {

    lateinit var image: ImageView
    lateinit var name: EditText
    lateinit var type: Spinner
    lateinit var amount: EditText
    lateinit var temp: EditText
    lateinit var time: EditText
    lateinit var measurement: Spinner
    var pickedImage: Boolean = false
    val pickImage = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tea)
        title = "Add a Tea"

        // Retrieving references to the edit fields in the activity
        image = findViewById<ImageView>(R.id.tea_image)
        name = findViewById<EditText>(R.id.tea_name)
        type = findViewById<Spinner>(R.id.tea_type)
        amount = findViewById<EditText>(R.id.tea_ammt)
        temp = findViewById<EditText>(R.id.tea_temp)
        time = findViewById<EditText>(R.id.tea_time)
        measurement = findViewById<Spinner>(R.id.measurement)

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

        image.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        /* Binding add button */

        val button = findViewById<Button>(R.id.add_button)
        button.setOnClickListener {
            if(name.text.toString() == "" ||
                amount.text.toString() == "" ||
                temp.text.toString() == "" ||
                time.text.toString() == "" || !pickedImage
            ) {
                val toast = Toast.makeText(this,
                    "Invalid input: fill all fields", Toast.LENGTH_SHORT)
                toast.show()
            } else {
                val dbOps = DatabaseOperations(this)
                dbOps.addTea(this,
                    name.text.toString(),
                    selectedType,
                    amount.text.toString() + " " + selectedMeasurement,
                    (temp.text.toString()).toInt(),
                    (time.text.toString()).toInt(),
                    imageToBitmap(image))
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /* Getting the image from the image selection intent */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            var imageUri = data?.data
            image.setImageURI(imageUri)
            pickedImage = true
        }
    }

    /**
     * imageToBitmap:
     * @desc takes in an ImageView, and will return a ByteArray representing the image
     * @param image ImageView that will be converted to ByteArray
     * @return ByteArray
     */
    private fun imageToBitmap(image: ImageView): ByteArray {
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)

        return stream.toByteArray()
    }
}
