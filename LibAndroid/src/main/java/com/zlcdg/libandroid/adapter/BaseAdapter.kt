package com.zlcdg.libandroid.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.properties.Delegates

/**
 * 所有RecyclerView的适配器类
 */
abstract class BaseAdapter<M> : RecyclerView.Adapter<BaseViewHolder>() {

    private val mViewArray = SparseArray<View>()

    private val mData: MutableList<M> = Collections.synchronizedList(mutableListOf())

    private var mInflater: LayoutInflater by Delegates.notNull()

    private var mClickListener: OnItemClickListener? = null
    private var mLongClickListener: OnItemLongClickListener? = null

    var isEdit = false

    var emptyView: View
        get() = getView(VIEW_TYPE_EMPTY)
        set(emptyView) = setView(VIEW_TYPE_EMPTY, emptyView)

    var headerView: View
        get() = getView(VIEW_TYPE_HEADER)
        set(headerView) = setView(VIEW_TYPE_HEADER, headerView)

    var footerView: View
        get() = getView(VIEW_TYPE_FOOTER)
        set(footerView) = setView(VIEW_TYPE_FOOTER, footerView)

    private val extraViewCount: Int
        get() {
            var extraViewCount = 0
            if (mViewArray.get(VIEW_TYPE_HEADER) != null) {
                extraViewCount++
            }
            if (mViewArray.get(VIEW_TYPE_FOOTER) != null) {
                extraViewCount++
            }
            return extraViewCount
        }

    val dataSize: Int
        get() = mData.size

    fun setData(data: List<M>?) {
        mData.clear()
        data?.let {
            mData.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun getData(): List<M> {
        return mData
    }

//    fun remove(m: M) {
//        try {
//            mData.remove(m)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        notifyDataSetChanged()
//    }

//    fun add(m: M) {
//        mData.add(m)
//        notifyDataSetChanged()
//    }


    fun get(position: Int): M? {
        return try {
            mData[position]
        } catch (e: Exception) {
            null
        }
    }

    fun clear() {
        mData.clear()
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSize == 0 && mViewArray.get(VIEW_TYPE_EMPTY) != null) {
            VIEW_TYPE_EMPTY
        } else if (position == 0 && mViewArray.get(VIEW_TYPE_HEADER) != null) {
            VIEW_TYPE_HEADER
        } else if (position == itemCount - 1 && mViewArray.get(VIEW_TYPE_FOOTER) != null) {
            VIEW_TYPE_FOOTER
        } else {
            getCustomViewType()
        }
    }

    private fun getCustomViewType(): Int {
        return VIEW_TYPE_DEFAULT
    }

    protected abstract fun bindLayout(viewType: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (parent.context != null) {
            mInflater = LayoutInflater.from(parent.context)
        }

        var itemView: View? = mViewArray.get(viewType)
        if (itemView == null) {
            itemView = inflateLayout(bindLayout(viewType), parent)
        }
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_EMPTY, VIEW_TYPE_HEADER, VIEW_TYPE_FOOTER -> {
            }
            else -> bindCustomViewHolder(holder, position)
        }
    }

    private fun bindCustomViewHolder(holder: BaseViewHolder, position: Int) {
        val dataPos = position - if (mViewArray.get(VIEW_TYPE_HEADER) == null) 0 else 1
        holder.itemView.setOnClickListener { v ->
            if (mClickListener != null) {
                mClickListener!!.onItemClick(v, dataPos)
            }
        }
        holder.itemView.setOnLongClickListener { v ->
            mLongClickListener != null && mLongClickListener!!.onItemLongClick(
                v,
                dataPos
            )
        }

        if (dataPos > -1) {
            bind(holder, mData[dataPos], dataPos)
        }
    }

    protected abstract fun bind(holder: BaseViewHolder, data: M)

    open fun bind(holder: BaseViewHolder, data: M, position: Int) {
        bind(holder, data)
    }

    override fun getItemCount(): Int {
        return dataSize + extraViewCount
    }

    fun removeEmptyView() {
        removeView(VIEW_TYPE_EMPTY)
    }

    fun removeHeaderView() {
        removeView(VIEW_TYPE_HEADER)
    }

    fun removeFooterView() {
        removeView(VIEW_TYPE_FOOTER)
    }

    private fun setView(type: Int, view: View) {
        mViewArray.put(type, view)
        notifyDataSetChanged()
    }

    private fun getView(type: Int): View {
        return mViewArray.get(type)
    }

    private fun removeView(type: Int) {
        if (mViewArray.get(type) != null) {
            mViewArray.delete(type)
            notifyDataSetChanged()
        }
    }

    private fun inflateLayout(@LayoutRes layoutId: Int, parent: ViewGroup): View {
        return mInflater.inflate(layoutId, parent, false)
    }

    fun setOnItemClickListener(clickListener: OnItemClickListener) {
        mClickListener = clickListener
    }

    fun setOnItemLongClickListener(longClickListener: OnItemLongClickListener) {
        mLongClickListener = longClickListener
    }

    open fun moveChanged(datas: List<M>) {}

    fun itemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(mData, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(mData, i, i - 1)
            }
        }
        moveChanged(mData)
        notifyItemMoved(fromPosition, toPosition)
    }


    companion object {
        const val VIEW_TYPE_EMPTY = 0xfff0
        const val VIEW_TYPE_HEADER = 0xfff1
        const val VIEW_TYPE_FOOTER = 0xfff2
        const val VIEW_TYPE_DEFAULT = 0xfff3
    }
}
