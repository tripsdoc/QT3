package com.hsc.qda.utilities.image

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ImageTagAdapter(
    val context: Context,
    val callBack: (Any) -> Unit
): RecyclerView.Adapter<ImageTagAdapter.ViewHolder>() {

    private var imageList: ArrayList<String> = ArrayList()

    open class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = View(context)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = imageList[position]
        //Glide.with(context).load(model).into(holder.itemView.tagImage)
        /*holder.itemView.tagImage.setOnClickListener {
            callBack(model)
        }*/
    }

    fun updateList(list: List<String>) {
        imageList.addAll(list)
        notifyDataSetChanged()
    }

    fun clearList() {
        imageList.clear()
        notifyDataSetChanged()
    }
}