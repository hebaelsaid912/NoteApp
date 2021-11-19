package com.example.android.noteapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.noteapp.adabters.NotesAdabter
import com.example.android.noteapp.dataBase.NotesDataBase
import com.example.android.noteapp.databinding.FragmentHomeBinding
import com.example.android.noteapp.entities.Notes
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : BaseFragment() {
    lateinit var binding : FragmentHomeBinding
    var arrNotes = ArrayList<Notes>()
    lateinit var notesAdapter: NotesAdabter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate<FragmentHomeBinding>(inflater,
            R.layout.fragment_home,container,false)
        binding.searchView.queryHint = getString(R.string.search_hint).toString()


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.noteRv.hasFixedSize()
        binding.noteRv.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

        launch {
            context?.let {
                var notes = NotesDataBase.getDataBase(it).noteDeo().getAllNotes()
                arrNotes = notes as ArrayList<Notes>
                notesAdapter = NotesAdabter()
                notesAdapter.setData(arrNotes)
                notesAdapter.setOnClickListener(onClicked)
                binding.noteRv.adapter = notesAdapter
            }
        }

        binding.addNewNoteFab.setOnClickListener{
            var fragment:Fragment
            var bundle = Bundle()
            bundle.putInt("noteID",-1)
            fragment = CreateNoteFragment.newInstance()
            fragment.arguments = bundle
            replaceFragments(fragment,false)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(p0: String?): Boolean {
                var tempArr = ArrayList<Notes>()

                for (arr in arrNotes){
                    if (arr.title!!.lowercase(Locale.getDefault()).contains(p0.toString())){
                        tempArr.add(arr)
                    }
                }

                notesAdapter.setData(tempArr)
                notesAdapter.notifyDataSetChanged()
                return true
            }


        })

    }
    private val onClicked = object : NotesAdabter.onItemClickListener{
        override fun onClick(noteId: Int) {
            var fragment:Fragment
            var bundle = Bundle()
            bundle.putInt("noteID",noteId)
            fragment = CreateNoteFragment.newInstance()
            fragment.arguments = bundle
            replaceFragments(fragment,false)
        }

    }
    private fun replaceFragments(fragment: Fragment, isTransition: Boolean){
        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
        if (isTransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        Log.d("onHomeFragment","hereeeeeee")
        fragmentTransition.replace(R.id.fragment_container,fragment).addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()


    }
}