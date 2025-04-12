package com.tuanha.dapter

import com.tuanha.adapter.ViewItemAdapter
import com.tuanha.adapter.annotation.ItemAdapter
import com.tuanha.adapter.entities.ViewItem
import com.tuanha.app.databinding.ItemTest2Binding

@ItemAdapter
class Test2Adapter : ViewItemAdapter<Test2ViewItem, ItemTest2Binding>() {

    override fun onBindViewHolder(binding: ItemTest2Binding, viewType: Int, position: Int, item: Test2ViewItem, payloads: MutableList<Any>) {
        super.onBindViewHolder(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
    }

    override fun onBindViewHolder(binding: ItemTest2Binding, viewType: Int, position: Int, item: Test2ViewItem) {
        super.onBindViewHolder(binding, viewType, position, item)

        refreshText(binding, item)
    }

    private fun refreshText(binding: ItemTest2Binding, item: Test2ViewItem) {
        binding.tvText.text = item.text
    }
}

data class Test2ViewItem(
    val id: String,
    val text: String
) : ViewItem {

    override fun areItemsTheSame(): List<Any> = listOf(
        id
    )

    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
        text to PAYLOAD_TEXT
    )
}

private const val PAYLOAD_TEXT = "TEXT"