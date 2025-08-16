package com.example.simplenoteapp
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteRVAdapter(
    val context: Context,
    val noteClickInterface: NoteClickInterface,
    val noteClickDeleteInterface: NoteClickDeleteInterface

) : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.note_rv_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder,position: Int) {
        holder.noteTitle?.text = allNotes[position].noteTitle
//        holder.noteTimeStamp?.text ="Last Updated: " + allNotes[position].timeStamp

        val timeStamp = allNotes[position].timeStamp
        holder.noteTimeStamp?.text = context.getString(R.string.note_last_updated, timeStamp)

        holder.noteDelete?.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotes[position])
        }
        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes[position])
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

    private val allNotes = ArrayList<Note>()
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView? = itemView.findViewById<TextView>(R.id.tvNoteTitle)
        val noteTimeStamp: TextView? = itemView.findViewById<TextView>(R.id.tvDate)
        val noteDelete: ImageView? = itemView.findViewById<ImageView>(R.id.ivDelete)
    }
}

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(note: Note)
}