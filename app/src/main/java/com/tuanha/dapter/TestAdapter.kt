//package com.tuanha.dapter
//
//import com.tuanha.adapter.ViewItemAdapter
//import com.tuanha.adapter.annotation.ItemAdapter
//import com.tuanha.adapter.entities.ViewItem
//import com.tuanha.app.databinding.ItemTestBinding
//
//@ItemAdapter
//class TestAdapter : ViewItemAdapter<TestViewItem, ItemTestBinding>() {
//
//    override fun onBindViewHolder(binding: ItemTestBinding, viewType: Int, position: Int, item: TestViewItem, payloads: MutableList<Any>) {
//        super.onBindViewHolder(binding, viewType, position, item, payloads)
//
//        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
//    }
//
//    override fun onBindViewHolder(binding: ItemTestBinding, viewType: Int, position: Int, item: TestViewItem) {
//        super.onBindViewHolder(binding, viewType, position, item)
//
//        refreshText(binding, item)
//    }
//
//    private fun refreshText(binding: ItemTestBinding, item: TestViewItem) {
//        binding.tvText.text = item.text
//    }
//}
//
//data class TestViewItem(
//    val id: String,
//    val text: String
//) : ViewItem {
//
//    override fun areItemsTheSame(): List<Any> = listOf(
//        id
//    )
//
//    override fun getContentsCompare(): List<Pair<Any, String>> = listOf(
//        text to PAYLOAD_TEXT
//    )
//}
//
//private const val PAYLOAD_TEXT = "TEXT"