package com.example.simplenoteapp.ui.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.simplenoteapp.R
import com.example.simplenoteapp.data.model.Note
import com.example.simplenoteapp.databinding.ActivityMainBinding
import com.example.simplenoteapp.databinding.FragmentAudioNoteBinding
import com.example.simplenoteapp.ui.activity.MainActivity
import com.example.simplenoteapp.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects


class AudioNoteFragment : Fragment() {
    lateinit var noteViewModel: NoteViewModel
    var noteId = -1
    private lateinit var binding: FragmentAudioNoteBinding
    val REQUEST_CODE_SPEECH_INPUT: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioNoteBinding.inflate(inflater, container, false)

        val args = arguments
        val edit = args?.containsKey("noteId") == true
        val nId = args?.getInt("noteId") ?: -1
        val noteTitle = args?.getString("noteTitle") ?: ""
        val noteDescription = args?.getString("noteDescription") ?: ""
        binding.ivMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault()
            )

            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                Toast
                    .makeText(
                        context, " " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }

        noteViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[NoteViewModel::class.java]
//        val noteType = intent.getStringExtra("noteType")
//        if (noteType.equals("Edit")) {
//            val noteTitle = intent.getStringExtra("noteTitle")
//            val noteDescription = intent.getStringExtra("noteDescription")
//            noteId = intent.getIntExtra("noteId", -1)
//            binding.btnSave.text = this.getString(R.string.update_note)
//            binding.etNoteName.setText(noteTitle)
//            binding.etSpeechToText.setText(noteDescription)
//        } else {
//            binding.btnSave.text = this.getString(R.string.save_note)
//        }

        // Check if this is edit mode
        val noteType = activity?.intent?.getStringExtra("noteType")
        val isEdit = noteType == "Edit"
        if (isEdit) {
            binding.etNoteName.setText(noteTitle)
            binding.etSpeechToText?.setText(noteDescription)
            binding.btnSave.text = getString(R.string.update_note)

            val noteTitle = activity?.intent?.getStringExtra("noteTitle") ?: ""
            val noteDescription = activity?.intent?.getStringExtra("noteDescription") ?: ""
            noteId = activity?.intent?.getIntExtra("noteId", -1) ?: -1

            binding.etNoteName.setText(noteTitle)
            binding.etSpeechToText.setText(noteDescription)
            binding.btnSave.text = getString(R.string.update_note)
        } else {
            binding.btnSave.text = getString(R.string.save_note)
        }


        binding.btnSave.setOnClickListener {
            val noteTitle = binding.etNoteName.text.toString()
            val noteDescription = binding.etSpeechToText.text.toString()

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

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )
                var speakable = Objects.requireNonNull<ArrayList<String>?>(result)[0]
                binding.etSpeechToText.setText(speakable)
            }
        }
    }

}