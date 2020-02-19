package com.zlcdg.libandroid.adapter

import android.view.View

/**
 *@author  裴存亮（peicl@zlcdgroup.com）
 *@date 2018/7/11 17:18
 */
interface OnItemLongClickListener {
    fun onItemLongClick(view: View, position: Int): Boolean
}