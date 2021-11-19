package com.example.android.noteapp.adabters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.noteapp.R
import com.example.android.noteapp.entities.Notes
import com.makeramen.roundedimageview.RoundedImageView
import java.util.ArrayList

class NotesAdabter():
    RecyclerView.Adapter<NotesAdabter.NoteViewHolder>() {

    var notesList = ArrayList<Notes>()
    var listener:onItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {

        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_notes_items,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.title.text = notesList[position].title.toString()
        holder.subTitle.text = notesList[position].subTitle.toString()
        holder.noteDate.text = notesList[position].noteData.toString()
        Log.d("CreateNoteFragmentAdapter:" , "Title : " +notesList[position].title.toString())
        Log.d("CreateNoteFragmentAdapter:" , "sub Title : " +notesList[position].subTitle.toString())
        Log.d("CreateNoteFragmentAdapter:" , "note Data : " +notesList[position].noteData.toString())

        if(!notesList[position].noteColor.isNullOrEmpty()){
            Log.d("CreateNoteFragmentAdapter:" , "note color : " +notesList[position].noteColor)
            holder.cardView.setBackgroundResource(Integer.parseInt(notesList[position].noteColor))
        }else{
            holder.cardView.setBackgroundResource(Integer.parseInt(R.color.light_purple.toString()))
        }
        if (!notesList[position].imagePath.isNullOrEmpty()){
            Log.d("CreateNoteFragmentAdapter:" , "note image path : " +notesList[position].imagePath)
            holder.noteImage.setImageBitmap(BitmapFactory.decodeFile(notesList[position].imagePath))
            holder.noteImage.visibility = View.VISIBLE
        }else{
            holder.noteImage.visibility = View.GONE
        }
        if (!notesList[position].webLink.isNullOrEmpty()){
            Log.d("CreateNoteFragmentAdapter:" , "note web link : " +notesList[position].webLink)
            holder.noteLink.text = notesList[position].webLink
            holder.noteLink.visibility = View.VISIBLE
        }else{
            holder.noteLink.visibility = View.GONE
        }

        holder.cardView.setOnClickListener {
            listener!!.onClick(notesList[position].id!!)
        }

    }

    override fun getItemCount(): Int {
        Log.d("CreateNoteFragmentNoteAdapter : " , notesList.size.toString()+"")
        return notesList.size
    }
    fun setData( notesList: List<Notes>){
        this.notesList = notesList as ArrayList<Notes>
    }
    fun setOnClickListener(listener: onItemClickListener){
        this.listener = listener
    }
    class NoteViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val title:TextView = itemView.findViewById(R.id.home_title_rv_tv)
        val subTitle:TextView = itemView.findViewById(R.id.home_sub_title_rv_tv)
        val noteDate:TextView = itemView.findViewById(R.id.home_note_data_rv_tv)
        val cardView:CardView = itemView.findViewById(R.id.card_view_rv)
        val noteImage:RoundedImageView = itemView.findViewById(R.id.imgNote)
        val noteLink:TextView = itemView.findViewById(R.id.home_note_web_link_tv)
    }

    interface onItemClickListener{
        fun onClick(notesId:Int)
    }
}