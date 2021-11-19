package com.example.android.noteapp.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.noteapp.R
import com.example.android.noteapp.databinding.FragmentCreateNoteBinding
import com.example.android.noteapp.databinding.FragmentNotesBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoteBottomSheetFragment :BottomSheetDialogFragment() {

    lateinit var binding: FragmentNotesBottomSheetBinding
    var selectedColor = R.color.ColorDefaultNote
    var BLUE:String = "BLUE"
    var YELLOW:String = "YELLOW"
    var WHITE:String = "WHITE"
    var PURPLE:String = "PURPLE"
    var GREEN:String = "GREEN"
    var ORANGE:String = "ORANGE"
    var BLACK:String = "BLACK"
    var WEB_LINK:String = "WEB_LINK"
    var IMAGE_LINK:String = "IMAGE_LINK"
    var DELETE_THIS_NOTE:String = "DELETE_THIS_NOTE"


    companion object {
        var noteId:Int = -1
        fun newInstance(noteId:Int): NoteBottomSheetFragment{
            val args = Bundle()
            val fragment = NoteBottomSheetFragment()
            fragment.arguments = args
            Log.d("NoteBottomSheetFragment : " ,"onViewCreated ${Companion.noteId}")
            this.noteId = noteId
            return fragment
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = layoutInflater.inflate(R.layout.fragment_notes_bottom_sheet,null)

        dialog.setContentView(view)
        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = param.behavior
        if(behavior is BottomSheetBehavior<*>){
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state=""
                    when(newState){
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =DataBindingUtil.inflate<FragmentNotesBottomSheetBinding>(inflater,
            R.layout.fragment_notes_bottom_sheet,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (noteId != -1){
            Log.d("NoteBottomSheetFragment : " ,"onViewCreated $noteId")
            binding.deleteThisNoteLin.visibility = View.VISIBLE
        }else{
            binding.deleteThisNote.visibility = View.GONE
        }
        setListener()
    }

    private fun setListener(){
        binding.fNote1.setOnClickListener {
            binding.fNote1Color.setImageResource(R.drawable.note_color_1)
            binding.fNote2Color.setImageResource(0)
            binding.fNote3Color.setImageResource(0)
            binding.fNote4Color.setImageResource(0)
            binding.fNote5Color.setImageResource(0)
            binding.fNote6Color.setImageResource(0)
            binding.fNote7Color.setImageResource(0)
            selectedColor = R.color.ColorBlueNote
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",BLUE)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
           // dismiss()
        }
        binding.fNote2.setOnClickListener {
            binding.fNote1Color.setImageResource(0)
            binding.fNote2Color.setImageResource(R.drawable.note_color_2)
            binding.fNote3Color.setImageResource(0)
            binding.fNote4Color.setImageResource(0)
            binding.fNote5Color.setImageResource(0)
            binding.fNote6Color.setImageResource(0)
            binding.fNote7Color.setImageResource(0)
            selectedColor = R.color.ColorYellowNote

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",YELLOW)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            //dismiss()
        }
        binding.fNote3.setOnClickListener {
            binding.fNote1Color.setImageResource(0)
            binding.fNote2Color.setImageResource(0)
            binding.fNote3Color.setImageResource(R.drawable.note_color_3)
            binding.fNote4Color.setImageResource(0)
            binding.fNote5Color.setImageResource(0)
            binding.fNote6Color.setImageResource(0)
            binding.fNote7Color.setImageResource(0)
            selectedColor = R.color.light_purple
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",WHITE)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            //dismiss()
        }
        binding.fNote4.setOnClickListener {
            binding.fNote1Color.setImageResource(0)
            binding.fNote2Color.setImageResource(0)
            binding.fNote3Color.setImageResource(0)
            binding.fNote4Color.setImageResource(R.drawable.note_color_4)
            binding.fNote5Color.setImageResource(0)
            binding.fNote6Color.setImageResource(0)
            binding.fNote7Color.setImageResource(0)
            selectedColor = R.color.ColorPurpleNote
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",PURPLE)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            //dismiss()
        }
        binding.fNote5.setOnClickListener {
            binding.fNote1Color.setImageResource(0)
            binding.fNote2Color.setImageResource(0)
            binding.fNote3Color.setImageResource(0)
            binding.fNote4Color.setImageResource(0)
            binding.fNote5Color.setImageResource(R.drawable.note_color_5)
            binding.fNote6Color.setImageResource(0)
            binding.fNote7Color.setImageResource(0)
            selectedColor = R.color.ColorGreenNote
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",GREEN)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            //dismiss()
        }
        binding.fNote6.setOnClickListener {
            binding.fNote1Color.setImageResource(0)
            binding.fNote2Color.setImageResource(0)
            binding.fNote3Color.setImageResource(0)
            binding.fNote4Color.setImageResource(0)
            binding.fNote5Color.setImageResource(0)
            binding.fNote6Color.setImageResource(R.drawable.note_color_6)
            binding.fNote7Color.setImageResource(0)
            selectedColor = R.color.ColorOrangeNote
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",ORANGE)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            //dismiss()
        }
        binding.fNote7.setOnClickListener {
            binding.fNote1Color.setImageResource(0)
            binding.fNote2Color.setImageResource(0)
            binding.fNote3Color.setImageResource(0)
            binding.fNote4Color.setImageResource(0)
            binding.fNote5Color.setImageResource(0)
            binding.fNote6Color.setImageResource(0)
            binding.fNote7Color.setImageResource(R.drawable.note_color_7)
            selectedColor = R.color.ColorBlackNote
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",BLACK)
            intent.putExtra("selectedColor",selectedColor.toString())
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
           // dismiss()
        }
        binding.addNoteImageLin.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",IMAGE_LINK)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        binding.addNoteLinkLin.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",WEB_LINK)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
        binding.deleteThisNote.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionNote",DELETE_THIS_NOTE)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }
    }

}