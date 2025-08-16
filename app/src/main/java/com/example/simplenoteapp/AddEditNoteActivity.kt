package com.example.simplenoteapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.simplenoteapp.databinding.ActivityAddEditNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddEditNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddEditNoteBinding
    lateinit var noteViewModel: NoteViewModel
    var noteId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        noteViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NoteViewModel::class.java)
        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            //on below line we are setting data to edit text.
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteId = intent.getIntExtra("noteId", -1)
            binding.btnSave.text = "Update Note"
            binding.etNoteName.setText(noteTitle)
            binding.etNoteDesc.setText(noteDescription)
        } else {
            binding.btnSave.text = "Save Note"
        }

        binding.btnSave.setOnClickListener {
            //on below line we are getting title and desc from edit text.
            val noteTitle = binding.etNoteName.text.toString()
            val noteDescription = binding.etNoteDesc.text.toString()
            //on below line we are checking the type and then saving or updating the data.
            if (noteType.equals("Edit")) {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    val updatedNote = Note(noteTitle, noteDescription, currentDateAndTime)
                    updatedNote.id = noteId
                    noteViewModel.updateNote(updatedNote)
                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
                }
            } else {
                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDateAndTime: String = sdf.format(Date())
                    //if the string is not empty we are calling a add note method to add data to our room database.
                    noteViewModel.addNote(Note(noteTitle, noteDescription, currentDateAndTime))
                    Toast.makeText(this, "$noteTitle Added", Toast.LENGTH_LONG).show()
                }
            }
            //opening the new activity on below line
            startActivity(Intent(applicationContext, MainActivity::class.java))
            this.finish()
        }
    }
}