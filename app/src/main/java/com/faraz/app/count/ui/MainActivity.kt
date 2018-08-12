package com.faraz.app.count.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SwitchCompat
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EdgeEffect
import android.widget.EditText
import android.widget.Toast
import com.faraz.app.count.R
import com.faraz.app.count.component
import com.faraz.app.count.data.AppRxSchedulers
import com.faraz.app.count.data.Filter
import com.faraz.app.count.extensions.KotlinRVAdapter
import com.faraz.app.count.extensions.vmProviderForActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.function.Predicate
import javax.inject.Inject

class MainActivity : AppCompatActivity(),HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var appRxSchedulers: AppRxSchedulers
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingAndroidInjector

    private val mainVM by vmProviderForActivity { component.mainVM }

    private lateinit var wordsAdapter: KotlinRVAdapter<String,WordsVH>
    private var prefix = "http://www."
    private  var filter = Filter(false,-1,false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        getWords()

        btnFetch.setOnClickListener {
            if(etUrl.text.isNotEmpty()) {

                getWords(etUrl.text.toString())
            }
            else
                Toast.makeText(this,"Enter a url",Toast.LENGTH_SHORT).show()
        }

        fabFilter.setOnClickListener {
            showFilerDialog()
        }
    }

    private fun initView() {
        etUrl.setText(prefix)
        Selection.setSelection(etUrl.text,etUrl.text.length)

        etUrl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(!s.toString().startsWith(prefix)){
                    etUrl.setText(prefix)
                    Selection.setSelection(etUrl.text, etUrl.getText().length)

                }
            }

        })
    }

    private fun showFilerDialog() {
        val dialogBuilder = AlertDialog.Builder(this)

        val inflater = this.layoutInflater

        val dialogView = inflater.inflate(R.layout.layout_filter, null)
        dialogBuilder.setView(dialogView)

        val switchCommon = dialogView.findViewById<SwitchCompat>(R.id.sCommonWords)
        val etWordLimit = dialogView.findViewById<EditText>(R.id.etWords)
        val switchLength = dialogView.findViewById<SwitchCompat>(R.id.sLength)

        switchCommon.isChecked = filter.removeCommonWords
        switchLength.isChecked = filter.removeShortWords
        if(filter.wordsLimit == -1) etWordLimit.setText("") else etWordLimit.setText("${filter.wordsLimit}")
        dialogBuilder.setTitle("Filters")
        dialogBuilder.setCancelable(true)
        dialogBuilder.setPositiveButton("Apply"){_,_ ->
            val input = etWordLimit.text.toString()
            val limit = if(input.isEmpty()) -1 else input.toInt()
            filter.removeCommonWords = switchCommon.isChecked
            filter.removeShortWords = switchLength.isChecked
            filter.wordsLimit = limit
            displayList(mainVM.applyFilters(filter))
        }
        dialogBuilder.show()
    }

    private fun  showProgressBar(show:Boolean){
        btnFetch.text = if(show) "" else "Fetch"
        pbFetch.visibility = if(show) View.VISIBLE else View.GONE
    }

    private fun getWords(url: String ="http://www.viacom18.com" ) {

        showProgressBar(true)
        mainVM.parseSource(url)?.subscribe({
            showProgressBar(false)
            Toast.makeText(this,"Loaded from ${it.source} ",Toast.LENGTH_SHORT).show()
            displayList(it.map)
        }, {
            showProgressBar(false)
            Toast.makeText(this,"Error loading data",Toast.LENGTH_SHORT).show()
            it.printStackTrace()
        })
    }


    private fun displayList(wordMap: Map<String, Int>) {

        if (wordMap.isEmpty()) {
            fabFilter.visibility = View.GONE
        } else {
            fabFilter.visibility = View.VISIBLE
        }
        val words= ArrayList(wordMap.keys)



        rvWordsCount.layoutManager = LinearLayoutManager(this)

        wordsAdapter = KotlinRVAdapter(this,
                R.layout.rv_word_count_item,
                {
                    view -> WordsVH(view)
                },{
            holder,item,position ->
            holder.bindWord(item,wordMap[item] ?: 0)
        },words)

        rvWordsCount.adapter = wordsAdapter
        wordsAdapter.notifyDataSetChanged()
    }
}
