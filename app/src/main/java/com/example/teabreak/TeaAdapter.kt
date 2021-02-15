package com.example.teabreak

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tea_item.view.*
import kotlinx.android.synthetic.main.tea_item.view.tea_name
import kotlinx.android.synthetic.main.tea_item.view.tea_temp
import kotlinx.android.synthetic.main.tea_item.view.tea_type


/**
 * TeaAdapter:
 *
 * @desc This class uses and ArrayList of TeaItem objects to populate a RecyclerView.
 * A ViewHolder is defined and cycled for each item in the database and is used
 * to fill in the fields in each TeaItem's CardView.
 *
 * @property context context of the MainActivity
 * @property teas an ArrayList of TeaItems this class uses to populate each CardView
 */

class TeaAdapter(private val context: Context,
                 private val teas: ArrayList<TeaItem>):
    RecyclerView.Adapter<TeaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnLongClickListener {

        private lateinit var tea: TeaItem
        private val nameTextView: TextView = itemView.findViewById(R.id.tea_name)
        private val typeTextView: TextView = itemView.findViewById(R.id.tea_type)
        private val tempTextView: TextView = itemView.findViewById(R.id.tea_temp)
        private val amountTextView: TextView = itemView.findViewById(R.id.tea_amount)
        private val timeTextView: TextView = itemView.findViewById(R.id.tea_time)
        private val brew: Button = itemView.findViewById(R.id.brew_button)

        init {
            itemView.setOnLongClickListener(this)
        }

        fun bind(tea: TeaItem) {
            this.tea = tea

            nameTextView.text = tea.name
            typeTextView.text = tea.type
            amountTextView.text = tea.amount
            tempTextView.text = tea.temp.toString() + "\u2109"
            timeTextView.text = tea.time.toString() + ":00"

            brew.setOnClickListener {
                val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                    putExtra(AlarmClock.EXTRA_LENGTH, (tea.time * 60))
                }
                itemView.context.startActivity(intent, null)
                Toast.makeText(itemView.context,
                    "Timer set for " + tea.time + " minutes.",
                    Toast.LENGTH_LONG).show()
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            /*
             * PLACEHOLDER FUNCTIONALITY
             * TODO: Add Dialog to prompt user before tea deletion
             */
            val dbOps = DatabaseOperations(itemView.context)
            dbOps.deleteTea(itemView.context, tea.getId())
            val intent = Intent(itemView.context, MainActivity::class.java)
            startActivity(itemView.context, intent, null)
            return true
        }
    }

    /**
     * onCreateViewHolder:
     * @desc inflates and returns the ViewHolder
     * @param parent
     * @param viewType
     * @return ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.tea_item,parent,false)
        return ViewHolder(v)
    }


    /**
     * getItemCount:
     * @desc returns the size of the teas ArrayList
     * @return Int
     */
    override fun getItemCount(): Int {
        return teas.size
    }


    /**
     * onBindViewHolder:
     * @desc binds all of the fields in the ViewHolder to the data associated
     * with the tea at the given position in the ArrayList, also binds
     * the brew and delete buttons
     * @param holder ViewHolder to be bound
     * @param position position in the teas ArrayList
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val tea: TeaItem = teas[position]
        holder.bind(tea)

        /*
        holder.delete.setOnClickListener {
            val dbOps = DatabaseOperations(context)
            dbOps.deleteTea(context, tea.getId())
            val intent = Intent(context, MainActivity::class.java)
            startActivity(context, intent, null)
        }
        */
    }

    

}