package com.faraz.app.count.ui

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.faraz.app.count.data.Filter
import com.faraz.app.count.data.ListData
import com.faraz.app.count.data.MainRepo
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Created by root on 11/8/18.
 */
class MainVM @Inject constructor(private val mainRepo: MainRepo):ViewModel() {

    private var sourceObservable: Observable< ListData>? = null
    private var previousWebsite = ""
    private lateinit var listData: ListData
    private var commonWords = listOf("of", "the" , "a", "an" , "with" , "he" , "she", "it" , "they" , "them", "in", "also" , "because", "us","is","to")
    private var listOfWords = ArrayList<String>()
    private var shortWordLength = 2

    fun parseSource(url :String): Observable< ListData>? {
        if(sourceObservable == null || previousWebsite != url) {
            previousWebsite = url
            sourceObservable = mainRepo.parseWebsite(url)
                    /**this function does a number of operations like
                     1. splitting the text based on the provided regex
                     2. count frequency of each word
                     3. sort the map in decreasing order of the count
                     4. we give back the Listdata type inorder to know which source we got the data from
                     */
                    .map { data ->

                val text = data.data
                listOfWords.clear()
                val list = text?.split(Regex("[^\\w]")) ?: emptyList()
                val counts = HashMap<String, Int>()
                for (item  in list) {
                    val word = item.trim().toLowerCase()
                    if (word.isNotEmpty()) {
                        val count = counts[word]
                        listOfWords.add(word)
                        if (count == null) {
                            counts[word] = 1
                        } else {
                            counts[word] = count + 1
                        }
                    }
                }

                val map =counts.toList().sortedByDescending { (_,value) -> value }.toMap()
//                     counts.toSortedMap(Comparator { value, value1 -> value.compareTo(value1) })
                listData = ListData(data.source,map)
                listData

            }
        }
        return  sourceObservable
    }


    fun applyFilters(filter: Filter) :Map<String, Int> {

        val counts = HashMap<String, Int>()
        for (text in listOfWords) {
            val word = text.trim().toLowerCase()
            if (word.isNotEmpty() ) {
                if(filter.removeCommonWords && commonWords.contains(word))
                    continue
                if(filter.removeShortWords && word.length <= shortWordLength)
                    continue
                val count = counts[word]
                if (count == null) {
                    counts[word] = 1
                } else {
                    counts[word] = count + 1
                }
            }
        }
        val size = if(filter.wordsLimit == -1) (counts.size-1) else filter.wordsLimit

        return counts.toList().sortedByDescending { (_,value) -> value }.subList(0,size).toMap()


    }


}