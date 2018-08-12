package com.faraz.app.count.extensions

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by root on 11/8/18.
 */
class KotlinRVAdapter<M, H : RecyclerView.ViewHolder>(

        private val context: Context?,
        private val layoutId: Int,
        private val holderMaker: (view: View) -> H,
        private val binder: ((holder: H, item: M, Int) -> Unit)) : RecyclerView.Adapter<H>() {

    private var layoutInflater = LayoutInflater.from(context)
    private var items: MutableList<M>? = null
    private var itemsArray: Array<M>? = null

    constructor(context: Context?,
                layoutId: Int,
                holderMaker: (view: View) -> H,
                binder: ((H, M, Int) -> Unit), items: MutableList<M>)
            : this(context, layoutId, holderMaker, binder) {

        this.items = items.toMutableList()
    }

    constructor(context: Context?,
                layoutId: Int,
                holderMaker: (view: View) -> H,
                binder: ((H, M, Int) -> Unit),
                itemsArray: Array<M>)
            : this(context, layoutId, holderMaker, binder) {

        this.itemsArray = itemsArray
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val view = layoutInflater.inflate(layoutId, parent, false)
        return holderMaker(view)
    }

    override fun onBindViewHolder(holder: H, position: Int) {
        val item: M = (if (items != null) items?.get(position)
        else itemsArray?.get(position))!!

//        d(item.toString())
        binder(holder, item, position)
    }

    override fun getItemCount(): Int {
        return (if (items != null) items?.size else itemsArray?.size)!!
    }

    fun removeItem(position: Int) {
        items?.removeAt(position)
        notifyItemRemoved(position)
    }

}