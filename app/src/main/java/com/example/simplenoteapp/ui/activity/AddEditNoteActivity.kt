package com.example.simplenoteapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.simplenoteapp.R
import com.example.simplenoteapp.databinding.ActivityAddEditNoteBinding
import com.example.simplenoteapp.ui.fragment.AudioNoteFragment
import com.example.simplenoteapp.ui.fragment.ImageNoteFragment
import com.example.simplenoteapp.ui.fragment.ListNoteFragment
import com.example.simplenoteapp.ui.fragment.TextNoteFragment

class AddEditNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddEditNoteBinding
//    lateinit var noteViewModel: NoteViewModel
//    var noteId = -1
    var noteStyle = ""
    @SuppressLint("SimpleDateFormat")
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
        noteStyle()

//        noteViewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        )[NoteViewModel::class.java]
//        val noteType = intent.getStringExtra("noteType")
//        if (noteType.equals("Edit")) {
//            val noteTitle = intent.getStringExtra("noteTitle")
//            val noteDescription = intent.getStringExtra("noteDescription")
//            noteId = intent.getIntExtra("noteId", -1)
//            binding.btnSave.text = this.getString(R.string.update_note)
//            binding.etNoteName.setText(noteTitle)
//            binding.etNoteDesc.setText(noteDescription)
//        } else {
//            binding.btnSave.text = this.getString(R.string.save_note)
//        }

//        binding.btnSave.setOnClickListener {
//            val noteTitle = binding.etNoteName.text.toString()
//            val noteDescription = binding.etNoteDesc.text.toString()
//            if (noteType.equals("Edit")) {
//                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
//                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
//                    val currentDateAndTime: String = sdf.format(Date())
//                    val updatedNote = Note(noteTitle, noteDescription, currentDateAndTime)
//                    updatedNote.id = noteId
//                    noteViewModel.updateNote(updatedNote)
//                    Toast.makeText(this, "Note Updated..", Toast.LENGTH_LONG).show()
//                }
//            } else {
//                if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
//                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
//                    val currentDateAndTime: String = sdf.format(Date())
//                    noteViewModel.addNote(Note(noteTitle, noteDescription, currentDateAndTime))
//                    Toast.makeText(this, "$noteTitle Added", Toast.LENGTH_LONG).show()
//                }
//            }
//            startActivity(Intent(applicationContext, MainActivity::class.java))
//            this.finish()
//        }
    }

    private fun noteStyle() {
        noteStyle = intent.getStringExtra("noteType").toString()
        when (noteStyle) {
            "audio" -> {
                replaceFragment(AudioNoteFragment())
            }
            "text" -> {
                replaceFragment(TextNoteFragment())
            }
            "list" -> {
                replaceFragment(ListNoteFragment())
            }
            "image" -> {
                replaceFragment(ImageNoteFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}