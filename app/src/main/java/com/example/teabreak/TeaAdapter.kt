package com.example.teabreak

import android.content.Context
import android.content.Intent
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tea_item.view.*
import kotlinx.android.synthetic.main.tea_item.view.tea_name
import kotlinx.android.synthetic.main.tea_item.view.tea_origin
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

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.tea_name
        val type = itemView.tea_type
        val origin = itemView.tea_origin
        val amount = itemView.tea_amount
        val temp = itemView.tea_temp
        val brew = itemView.brew_button
        val delete = itemView.delete_button
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

        holder.name.text = tea.name
        holder.type.text = tea.type
        holder.origin.text = tea.origin
        holder.amount.text = tea.amount
        holder.temp.text = tea.temp.toString() + "\u2109"

        holder.brew.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
                putExtra(AlarmClock.EXTRA_LENGTH, (tea.time * 60))
            }
            holder.itemView.context.startActivity(intent, null)
            Toast.makeText(context,
                "Timer set for " + tea.time + " minutes.",
                Toast.LENGTH_LONG).show()
        }

        holder.delete.setOnClickListener {
            val dbOps = DatabaseOperations(context)
            dbOps.deleteTea(context, tea.getId())
            val intent = Intent(context, MainActivity::class.java)
            startActivity(context, intent, null)
        }
    }


}