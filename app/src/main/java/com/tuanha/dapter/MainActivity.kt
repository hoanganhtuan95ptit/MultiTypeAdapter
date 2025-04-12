package com.tuanha.dapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuanha.adapter.entities.ViewItem
import com.tuanha.adapter.utils.exts.submitListAwait
import com.tuanha.app.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {

            val list = arrayListOf<ViewItem>()

            for (i in 0..10) {
                if (i % 2 == 0) list.add(TestViewItem(id = "$i", text = "index: $i"))
                else list.add(Test2ViewItem(id = "$i", text = "index: $i"))
            }

            binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerView.submitListAwait(list)
        }
    }
}