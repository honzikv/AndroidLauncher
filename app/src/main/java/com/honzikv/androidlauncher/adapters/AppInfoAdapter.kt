package com.honzikv.androidlauncher.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.AppInfo
import kotlinx.android.synthetic.main.app_icon.view.*

/**
 *
 */
class AppInfoAdapter(private val context: Context, filledList: List<AppInfo>) : BaseAdapter() {

    private val appInfoList: List<AppInfo> = filledList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        //Initialize view if it is null
        val view = if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                    as LayoutInflater
            inflater.inflate(R.layout.app_icon, parent, false)
        } else {
            convertView
        }

        //Set the imageView to drawable
        view.appIconImageView
            .setImageDrawable(appInfoList[position].iconDrawable)

        //Set text to icon label text
        view.appIconTextView.text = appInfoList[position].iconLabel

        //Set button action to launch app
        view.appIconConstraintLayout
            .setOnClickListener {
                val intent = context.packageManager
                    .getLaunchIntentForPackage(appInfoList[position].packageName)

                if (intent != null) {
                    context.startActivity(intent)
                }
            }

        return view
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