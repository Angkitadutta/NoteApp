package com.example.simplenoteapp.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.simplenoteapp.R
import com.example.simplenoteapp.adapter.NoteClickDeleteInterface
import com.example.simplenoteapp.adapter.NoteClickInterface
import com.example.simplenoteapp.adapter.NoteRVAdapter
import com.example.simplenoteapp.data.model.Note
import com.example.simplenoteapp.databinding.ActivityMainBinding
import com.example.simplenoteapp.viewmodel.NoteViewModel
import androidx.core.graphics.drawable.toDrawable
import androidx.transition.Visibility

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface, PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var noteViewModel: NoteViewModel
    private lateinit var popup: PopupMenu
    private var isOverlayVisible = false
    private var creationType = ""

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        popup = PopupMenu(this, binding.fabAddNote)
        popup.menuInflater.inflate(R.menu.home_pop_up_menu_items, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.setForceShowIcon(true)

        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) // 2 columns
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.rvNotes.layoutManager = staggeredGridLayoutManager
        binding.rvNotes.setHasFixedSize(true)

        val noteAdapter = NoteRVAdapter(this, this, this)
        binding.rvNotes.adapter = noteAdapter

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]
        noteViewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                noteAdapter.updateList(it)
            }
        })

        binding.fabAddNote.setOnClickListener {

            if (!isOverlayVisible) {
                // Show overlay and popup menu
                binding.overlay.visibility = View.VISIBLE
                binding.fabAddNote.setImageResource(R.drawable.icon_close)
                popup.show()
                isOverlayVisible = true

                // hide overlay when user taps outside
                popup.setOnDismissListener {
                    binding.overlay.visibility = View.GONE
                    binding.fabAddNote.setImageResource(R.drawable.icon_add)
                    isOverlayVisible = false
                }
            } else {
                // Hide overlay and popup menu
//                binding.overlay.visibility = View.GONE
//                binding.fabAddNote.setImageResource(R.drawable.icon_add)
                popup.dismiss()
//                isOverlayVisible = false
            }

//            binding.overlay.visibility = View.VISIBLE
//            binding.fabAddNote.setImageResource(R.drawable.icon_close)
//            val popup = PopupMenu(this, it)
//            popup.menuInflater.inflate(R.menu.home_pop_up_menu_items, popup.menu)
//            popup.setOnMenuItemClickListener(this@MainActivity)
//            popup.setForceShowIcon(true) // show icons immediately
//            popup.show()
//            val popup = PopupMenu(this, it)
//            val inflater: MenuInflater = popup.menuInflater
//            popup.setForceShowIcon(true)
//            inflater.inflate(R.menu.home_pop_up_menu_items, popup.menu)
//
//            // Force icons to show
////            try {
////                val fields = popup.javaClass.getDeclaredField("mPopup")
////                fields.isAccessible = true
////                val menuPopupHelper = fields.get(popup)
////                val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
////                val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
////                setForceIcons.invoke(menuPopupHelper, true)
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
//
//            popup.show()
//            PopupMenu(this, binding.fabAddNote).apply {
//                // MainActivity implements OnMenuItemClickListener.
//                setOnMenuItemClickListener(this@MainActivity)
//                inflate(R.menu.home_pop_up_menu_items)
//                show()
//            }
//            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
//            startActivity(intent)
//            this.finish()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.home_menu_item_audio -> {
                val intent = Intent(this, AddEditNoteActivity::class.java)
                intent.putExtra("noteType", "audio")
                creationType = "audio"
                startActivity(intent)
                true
            }
            R.id.home_menu_item_text -> {
                val intent = Intent(this, AddEditNoteActivity::class.java)
                intent.putExtra("noteType", "text")
                creationType = "text"
                startActivity(intent)
                true
            }
//            R.id.home_menu_item_list -> {
//                val intent = Intent(this, AddEditNoteActivity::class.java)
//                intent.putExtra("noteType", "list")
//                startActivity(intent)
//                true
//            }
//            R.id.home_menu_item_image -> {
//                val intent = Intent(this, AddEditNoteActivity::class.java)
//                intent.putExtra("noteType", "image")
//                startActivity(intent)
//                true
//            }
            else -> false
        }
    }


    override fun onDeleteIconClick(note: Note) {
        noteViewModel.deleteNote(note)
        Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", creationType)
        intent.putExtra("editMode", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
        this.finish()
    }
}