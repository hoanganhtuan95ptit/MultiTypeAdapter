package com.tuanha.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tuanha.adapter.base.BaseAsyncAdapter
import com.tuanha.adapter.base.BaseBindingViewHolder
import com.tuanha.adapter.entities.ViewItem
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class ViewItemAdapter<VI : ViewItem, VB : ViewBinding>(
    private val onItemClick: ((View, VI) -> Unit)? = null
) {


    open var adapter: BaseAsyncAdapter<*, *>? = null


    open val viewItemClass: Class<VI> by lazy {
        findGenericTypeAssignableTo(this::class.java, ViewItem::class.java) ?: throw IllegalStateException("Cannot determine ViewItem class")
    }


    open fun createViewBinding(parent: ViewGroup, viewType: Int): VB {

        val viewBindingClass: Class<VB> = findGenericTypeAssignableTo(this::class.java, ViewBinding::class.java) ?: throw IllegalStateException("Cannot determine ViewBinding class")

        return inflate(viewBindingClass, parent)
    }


    open fun createViewHolder(parent: ViewGroup, viewType: Int): BaseBindingViewHolder<@UnsafeVariance VB>? {

        val viewHolder = BaseBindingViewHolder(createViewBinding(parent, viewType), viewType)

        val binding = viewHolder.binding

        val onItemClick = onItemClick ?: return viewHolder

        binding.root.setOnClickListener { view ->

            getViewItem(viewHolder.bindingAdapterPosition)?.let { onItemClick.invoke(view, it) }
        }

        return viewHolder
    }

    open fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
    }

    open fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
    }

    open fun onViewAttachedToWindow(holder: BaseBindingViewHolder<ViewBinding>) {
    }

    open fun onViewDetachedFromWindow(holder: BaseBindingViewHolder<ViewBinding>) {
    }

    open fun onBindViewHolder(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI, payloads: MutableList<Any>) {
    }

    open fun onBindViewHolder(binding: @UnsafeVariance VB, viewType: Int, position: Int, item: @UnsafeVariance VI) {
    }

    protected fun getViewItem(position: Int) = adapter?.currentList?.getOrNull(position) as? VI


    @Suppress("UNCHECKED_CAST")
    private fun <T> findGenericTypeAssignableTo(startClass: Class<*>, targetSuperClass: Class<*>): Class<T>? {

        var currentClass: Class<*>? = startClass

        while (currentClass != null && currentClass != Any::class.java) {

            val genericSuperclass = currentClass.genericSuperclass

            if (genericSuperclass is ParameterizedType) {

                val typeArguments = genericSuperclass.actualTypeArguments

                for (type in typeArguments) if (type is Class<*> && targetSuperClass.isAssignableFrom(type)) {
                    return type as Class<T>
                }
            }

            currentClass = currentClass.superclass
        }

        return null
    }

    @Suppress("UNCHECKED_CAST")
    private fun <VB : ViewBinding> inflate(viewBindingClass: Class<VB>, parent: ViewGroup): VB {

        val methods = viewBindingClass.declaredMethods

        val inflateMethod = methods.firstOrNull { method ->
            method.parameterTypes.contentEquals(
                arrayOf(LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
            )
        }

        if (inflateMethod == null) {
            throw NoSuchMethodException("No method matching the inflate signature found")
        }

        inflateMethod.isAccessible = true
        return inflateMethod.invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
    }
}