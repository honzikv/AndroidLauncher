package com.honzikv.androidlauncher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.honzikv.androidlauncher.data.AppInfo

/**
 *
 */
class AppInfoAdapter(private val context: Context, filledList: List<AppInfo>) : BaseAdapter() {

    private val appInfoList: List<AppInfo> = filledList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater


        }
        TODO("finish")
    }

    override fun getItem(position: Int): Any {
        return appInfoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return appInfoList.size
    }
}