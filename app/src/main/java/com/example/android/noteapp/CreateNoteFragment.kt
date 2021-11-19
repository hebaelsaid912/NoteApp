package com.example.android.noteapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.PatternMatcher
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.android.noteapp.adabters.NotesAdabter
import com.example.android.noteapp.dataBase.NotesDataBase
import com.example.android.noteapp.databinding.FragmentCreateNoteBinding
import com.example.android.noteapp.entities.Notes
import com.example.android.noteapp.util.NoteBottomSheetFragment
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter
import java.util.regex.Pattern


class CreateNoteFragment : BaseFragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    lateinit var binding: FragmentCreateNoteBinding
    var BLUE: String = "BLUE"
    var YELLOW: String = "YELLOW"
    var WHITE: String = "WHITE"
    var PURPLE: String = "PURPLE"
    var GREEN: String = "GREEN"
    var ORANGE: String = "ORANGE"
    var BLACK: String = "BLACK"
    var WEB_LINK:String = "WEB_LINK"
    var IMAGE_LINK:String = "IMAGE_LINK"
    var DELETE_THIS_NOTE:String = "DELETE_THIS_NOTE"

    var selectedNoteColor = R.color.white.toString()
     lateinit var selectedImagePath:String
     lateinit var enteredWebLink :String
     var noteId = -1

    private var READ_STORAGE_PERM = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteId = requireArguments().getInt("noteID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentCreateNoteBinding>(
            inflater,
            R.layout.fragment_create_note, container, false
        )
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(noteId != -1){
            launch {
                context?.let {
                    var notes = NotesDataBase.getDataBase(it).noteDeo().getClickedNote(noteId)
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(notes.noteColor.toString()))
                    binding.noteTitleEt.setText(notes.title)
                    binding.noteSubTitleEt.setText(notes.subTitle)
                    binding.noteTextEt.setText(notes.noteData)
                    binding.noteDateTv.text = notes.noteDate
                    selectedNoteColor = notes.noteColor.toString()
                    if(!notes.imagePath.isNullOrEmpty()) {
                        binding.noteImage.setImageBitmap(BitmapFactory.decodeFile(notes.imagePath))
                        selectedImagePath = notes.imagePath.toString()
                        binding.noteImageLin.visibility = View.VISIBLE
                        binding.deleteImage.visibility = View.VISIBLE

                    }else{
                        selectedImagePath = ""
                        binding.noteImageLin.visibility = View.GONE
                    }
                    if(!notes.webLink.isNullOrEmpty()) {
                        binding.noteWebLinkEt.setText( notes.webLink)
                        enteredWebLink = notes.webLink.toString()
                        binding.noteWebLinkLin.visibility = View.VISIBLE
                        binding.noteDeleteLinkBtn.visibility = View.VISIBLE
                    }else{
                        enteredWebLink = ""
                        binding.noteWebLinkLin.visibility = View.GONE
                        binding.noteWebLinkTv.visibility = View.GONE
                        binding.noteDeleteLinkBtn.visibility = View.GONE
                    }
                }
            }
        }else{
            selectedImagePath = ""
            enteredWebLink = ""
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadCastReceiver, IntentFilter("bottom_sheet_action")
        )
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        binding.noteDateTv.text = currentDate
        binding.imgSaveBtn.setOnClickListener {
            if(noteId != -1){
               var isUpdated =  updateNote()
                if(isUpdated){
                    Log.d("CreateNoteFragment : " ,"Note updated successfully $isUpdated" )
                    Toast.makeText(requireContext(), "Note updated successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Log.d("CreateNoteFragment : " ,"Error when update Note! $isUpdated" )
                    Toast.makeText(requireContext(), "Error when update Note!", Toast.LENGTH_SHORT).show()
                }
            }else {
                if(selectedImagePath.isNullOrEmpty()) {
                    selectedImagePath = ""
                }
                if(enteredWebLink.isNullOrEmpty()) {
                    enteredWebLink = ""
                }
                   saveNote()
            }
        }
        binding.imgBackBtn.setOnClickListener {
            if(noteId != -1){
                noteId = -1
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.imageMore.setOnClickListener {
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(noteId)
            noteBottomSheetFragment.show(
                requireActivity().supportFragmentManager, "Note Bottom Sheet Fragment"
            )
        }
        binding.noteWebLinkAddBtn.setOnClickListener {
            var webLink = binding.noteWebLinkEt.text.toString().trim()
            Log.d("CreateNoteFragment : " , "webLink from noteWebLinkAddBtn action 1 : $webLink" )
           var isWebLinkValid =  checkWebLinkValidation(webLink)
            if(isWebLinkValid){
                Log.d("CreateNoteFragment : " , "webLink from noteWebLinkAddBtn action  2 : $webLink : $isWebLinkValid" )
                enteredWebLink = webLink
                binding.noteWebLinkTv.visibility = View.VISIBLE
                binding.noteWebLinkTv.text = webLink
            }
        }
        binding.noteWebLinkCancelBtn.setOnClickListener {
            binding.noteWebLinkLin.visibility = View.GONE
        }
        binding.noteWebLinkTv.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(enteredWebLink))
            startActivity(intent)
        }
        binding.deleteImage.setOnClickListener {
            binding.noteImageLin.visibility = View.GONE
            selectedImagePath = ""
        }
        binding.noteDeleteLinkBtn.setOnClickListener {
            binding.noteWebLinkLin.visibility = View.GONE
            enteredWebLink = ""
        }
    }
    private fun updateNote(): Boolean {
            launch {
                context?.let {
                    val notes = NotesDataBase.getDataBase(it).noteDeo().getClickedNote(noteId)
                    notes.title = binding.noteTitleEt.text.toString().trim()
                    notes.subTitle = binding.noteSubTitleEt.text.toString().trim()
                    notes.noteData = binding.noteTextEt.text.toString().trim()
                    notes.noteDate = binding.noteDateTv.text.toString().trim()
                    notes.noteColor = selectedNoteColor
                    if(!selectedImagePath.isNullOrEmpty()) {
                        Log.d("CreateNoteFragment : " , "selectedImagePath : $selectedImagePath")
                        notes.imagePath = selectedImagePath

                    }else{
                        selectedImagePath = ""
                        Log.d("CreateNoteFragment : " , "selectedImagePath : $selectedImagePath")
                        notes.imagePath = selectedImagePath

                    }
                    if( !enteredWebLink.isNullOrEmpty()){
                        Log.d("CreateNoteFragment : " , "enteredWebLink : $enteredWebLink")
                        notes.webLink = enteredWebLink
                    }else{
                        enteredWebLink  = ""
                        Log.d("CreateNoteFragment : " , "enteredWebLink : $enteredWebLink")
                        notes.webLink = enteredWebLink
                    }
                    NotesDataBase.getDataBase(it).noteDeo().updateNote(notes)
                    binding.noteTitleEt.setText("")
                    binding.noteSubTitleEt.setText("")
                    binding.noteTextEt.setText("")
                    binding.noteImageLin.visibility = View.GONE
                    binding.noteWebLinkLin.visibility = View.GONE
                    binding.noteWebLinkTv.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }

        return true
    }

    private fun saveNote(): Boolean{
        if (binding.noteTitleEt.text.isNullOrEmpty()) {
            Log.d("toast","Title")
            Toast.makeText(requireContext(), "Note Title is Required !", Toast.LENGTH_SHORT).show()

        }else if (binding.noteSubTitleEt.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Note Sub Title is Required !", Toast.LENGTH_SHORT).show()

        }else if (binding.noteTextEt.text.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Note Description is Required !", Toast.LENGTH_SHORT).show()

        }else {
            launch {
                val notes = Notes()
                notes.title = binding.noteTitleEt.text.toString().trim()
                notes.subTitle = binding.noteSubTitleEt.text.toString().trim()
                notes.noteData = binding.noteTextEt.text.toString().trim()
                notes.noteDate = binding.noteDateTv.text.toString().trim()
                notes.noteColor = selectedNoteColor
                notes.imagePath = selectedImagePath
                notes.webLink = enteredWebLink
                Log.d(
                    "CreateNoteFragment:",
                    "Title : " + binding.noteTitleEt.text.toString().trim()
                )
                Log.d(
                    "CreateNoteFragment:",
                    "sub Title : " + binding.noteSubTitleEt.text.toString().trim()
                )
                Log.d(
                    "CreateNoteFragment:",
                    "note Data : " + binding.noteTextEt.text.toString().trim()
                )
                Log.d(
                    "CreateNoteFragment:",
                    "noteDate : " + binding.noteDateTv.text.toString().trim()
                )
                Log.d("CreateNoteFragment:", "noteColor : $selectedNoteColor")
                Log.d("CreateNoteFragment:", "noteImagePath : $selectedImagePath")
                Log.d("CreateNoteFragment:", "noteImagePath : $enteredWebLink")
                context?.let {
                    NotesDataBase.getDataBase(it).noteDeo().insertNewNote(notes)
                    binding.noteTitleEt.setText("")
                    binding.noteSubTitleEt.setText("")
                    binding.noteTextEt.setText("")
                    binding.noteImageLin.visibility = View.GONE
                    binding.noteWebLinkLin.visibility = View.GONE
                    binding.noteWebLinkTv.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()

                }
            }
            return true
        }
        return false
    }
    private fun deleteNote(){

        launch {
            context?.let {
                NotesDataBase.getDataBase(it).noteDeo().deleteSpecificNote(noteId)
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }


    val broadCastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var actionNoteColor = p1!!.getStringExtra("actionNote")
            when (actionNoteColor!!) {
                BLUE -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 1  : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                YELLOW -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 2 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                WHITE -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 3 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                PURPLE -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 4 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                GREEN -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 5 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                ORANGE -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 6 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                BLACK -> {
                    selectedNoteColor = p1.getStringExtra("selectedColor").toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 7 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
                IMAGE_LINK -> {
                    readStorageTask()

                }
                WEB_LINK -> {
                    binding.noteWebLinkLin.visibility = View.VISIBLE
                }
                DELETE_THIS_NOTE -> {

                    deleteNote()
                }

                else -> {
                    selectedNoteColor = R.color.white.toString()
                    Log.d("CreateNoteFragment : ", "selectedNoteColor 8 : $selectedNoteColor")
                    binding.cardViewColor.setBackgroundResource(Integer.parseInt(selectedNoteColor))
                }
            }
        }

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadCastReceiver)
        super.onDestroy()
    }
    private fun checkWebLinkValidation( webUrl:String) :Boolean{
        Log.d("CreateNoteFragment : " , "webLink from checkWebLinkValidation : $webUrl" )
        if(!webUrl.isNullOrEmpty()){
            if(Patterns.WEB_URL.matcher(webUrl).matches()){
                binding.noteWebLinkLin.visibility = View.GONE
                return true
            }else{
                Toast.makeText(requireContext(),"URL is not valid",Toast.LENGTH_SHORT).show()
                return false
            }
        }else{
            Toast.makeText(requireContext(),"Web URL can not be empty !",Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun hasReadStoragePerm(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun readStorageTask() {
        if (hasReadStoragePerm()) {
            Toast.makeText(context, "permission granted", Toast.LENGTH_SHORT).show()
            pickImageFromGallery()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.storage_permission_text),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var selectedImageUrl = data.data
                    if (selectedImageUrl != null) {
                        try {
                            var inputStream =
                                requireActivity().contentResolver.openInputStream(selectedImageUrl)
                            var bitmap = BitmapFactory.decodeStream(inputStream)
                            binding.noteImage.setImageBitmap(bitmap)
                            binding.noteImageLin.visibility = View.VISIBLE
                            selectedImagePath = getPathFromUri(selectedImageUrl)!!
                        } catch (e: Exception) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }

            }
        }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }
}