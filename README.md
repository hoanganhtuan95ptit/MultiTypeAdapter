## MultiTypeAdapter - Android Library

MultiTypeAdapter là thư viện Android giúp đơn giản hóa việc xử lý RecyclerView với nhiều loại item khác nhau, giảm thiểu mã nguồn boilerplate và giúp tối ưu hiệu suất khi làm việc với các danh sách phức tạp.

## Tính Năng
Tự động phân biệt các loại item trong RecyclerView mà không cần phải viết nhiều logic getItemViewType().

Hỗ trợ nhiều kiểu item khác nhau trong một RecyclerView.

Cập nhật RecyclerView tối ưu: Chỉ cập nhật các phần tử thay đổi, giúp tăng hiệu suất.

Dễ dàng sử dụng: Không cần viết lại nhiều code cho từng kiểu item riêng biệt.

Hỗ trợ DiffUtil: Tự động so sánh và cập nhật những phần thay đổi trong danh sách.

## Cài Đặt
Thêm phụ thuộc vào file build.gradle của dự án:

1. Thêm vào build.gradle của dự án
```java
   dependencies {
   implementation 'com.example:multitypeadapter:1.0.0'
   }
```
2. Thêm vào build.gradle của module ứng dụng
```java
   dependencies {
   implementation 'com.example:multitypeadapter:1.0.0'
   annotationProcessor 'com.example:multitypeadapter-processor:1.0.0'
   }
   ```
## Hướng Dẫn Sử Dụng
1. Tạo item adapter
``` java
@ItemAdapter
class TestAdapter : ViewItemAdapter<TestViewItem, ItemTestBinding>() {

    override fun onBindViewHolder(binding: ItemTestBinding, viewType: Int, position: Int, item: TestViewItem, payloads: MutableList<Any>) {
        super.onBindViewHolder(binding, viewType, position, item, payloads)

        if (payloads.contains(PAYLOAD_TEXT)) refreshText(binding, item)
    }

    override fun onBindViewHolder(binding: ItemTestBinding, viewType: Int, position: Int, item: TestViewItem) {
        super.onBindViewHolder(binding, viewType, position, item)

        refreshText(binding, item)
    }

    private fun refreshText(binding: ItemTestBinding, item: TestViewItem) {
        binding.tvText.text = item.text
    }
}

data class TestViewItem(
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
```
2. Sử dụng cơ bản
```java

val list = arrayListOf<ViewItem>()

            for (i in 0..10) {
        if (i % 2 == 0) list.add(TestViewItem(id = "$i", text = "index: $i"))
        else list.add(Test2ViewItem(id = "$i", text = "index: $i"))
        }

binding.recyclerView.adapter = MultiAdapter()
binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerView.submitListAwait(list)
```
3. Sử dụng nâng cao
```java
val list = arrayListOf<ViewItem>()

for (i in 0..10) {
        if (i % 2 == 0) list.add(TestViewItem(id = "$i", text = "index: $i"))
        else list.add(Test2ViewItem(id = "$i", text = "index: $i"))
        }

// Khởi tạo adapter với sự kiện click và long click
val advancedAdapter = AdvancedAdapter(
        onItemClick = { testViewItem ->
                Toast.makeText(this, "Item clicked: ${testViewItem.text}", Toast.LENGTH_SHORT).show()
        },
        onItemLongClick = { testViewItem ->
                Toast.makeText(this, "Item long clicked: ${testViewItem.text}", Toast.LENGTH_SHORT).show()
        }
)

binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
binding.recyclerView.adapter = MultiAdapter(advancedAdapter)
binding.recyclerView.submitListAwait(list)
```