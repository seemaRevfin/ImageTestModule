package com.example.imagecapturemodule

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DatabaseActivity: AppCompatActivity() {

    private val newWordActivityRequestCode = 1
    private val imageViewModel: ImageViewModel by viewModels {
        ImageViewModelFactory((application as ImageDetailsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ImageDetailsAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, SendDataActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }


        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        /*imageViewModel.allWords.observe( this) { images ->
            // Update the cached copy of the words in the adapter.
            images.let { adapter.submitList(it) }
        }*/
    }


}
