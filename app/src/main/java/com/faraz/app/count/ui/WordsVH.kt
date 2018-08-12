package com.faraz.app.count.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.rv_word_count_item.view.*

/**
 * Created by root on 11/8/18.
 */
class WordsVH(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindWord(word:String, count: Int) {
        with(itemView) {
            tvWord.text = word
            tvCount.text = "$count"
        }
    }
}