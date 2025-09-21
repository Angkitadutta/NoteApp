package com.example.simplenoteapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.simplenoteapp.R
import com.example.simplenoteapp.data.model.Note
import com.example.simplenoteapp.databinding.FragmentTextNoteBinding
import com.example.simplenoteapp.ui.activity.MainActivity
import com.example.simplenoteapp.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date


class TextNoteFragment : Fragment() {

    private lateinit var binding: FragmentTextNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    var noteId = -1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTextNoteBinding.inflate(inflater, container, false)

        val args = arguments
        val edit = args?.containsKey("noteId") == true
        val nId = args?.getInt("noteId") ?: -1
        val noteTitle = args?.getString("noteTitle") ?: ""
        val noteDescription = args?.getString("noteDescription") ?: ""


        // Initialize ViewModel
        noteViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]

        // Check if edit mode
        val noteType = activity?.intent?.getStringExtra("noteType")
        val isEdit = noteType == "Edit"
        if (isEdit) {
            binding.etNoteName.setText(noteTitle)
            binding.etNoteDesc?.setText(noteDescription)
            binding.btnSave.text = getString(R.string.update_note)

            val noteTitle = activity?.intent?.getStringExtra("noteTitle") ?: ""
            val noteDescription = activity?.intent?.getStringExtra("noteDescription") ?: ""
            noteId = activity?.intent?.getIntExtra("noteId", -1) ?: -1

            binding.etNoteName.setText(noteTitle)
            binding.etNoteDesc.setText(noteDescription)
            binding.btnSave.text = getString(R.string.update_note)
        } else {
            binding.btnSave.text = getString(R.string.save_note)
        }

        binding.btnSave.setOnClickListener {
            val noteTitle = binding.etNoteName.text.toString()
            val noteDescription = binding.etNoteDesc.text.toString()

            if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                val currentDateAndTime: String = sdf.format(Date())
                noteViewModel.addNote(
                    Note(noteTitle, noteDescription, currentDateAndTime)
                )
                Toast.makeText(context, "$noteTitle Added", Toast.LENGTH_LONG).show()
            }

            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }
}
