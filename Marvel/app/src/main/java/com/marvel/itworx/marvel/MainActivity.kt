package com.marvel.itworx.marvel

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.marvel.itworx.marvel.R.id.charactersRecyclerView
import com.marvel.itworx.marvel.data.Character
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : AppCompatActivity() {

    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityViewModel = MainActivityViewModel(application)
        mainActivityViewModel.callNetworkManager()
        var adapter = mainActivityViewModel.setAdapter()
        charactersRecyclerView.adapter = adapter
        recyclerViewManager = LinearLayoutManager(this)
        charactersRecyclerView.layoutManager = recyclerViewManager
        handleSearchEditText()
        displayCharacter(adapter)
    }

    private fun displayCharacter(adapter: CharactersListAdapter) {
        adapter.characterListRecyclerViewAdapterListener = object : CharacterListRecyclerViewAdapterListener {
            override fun onItemClick(characterID: Int) {
                val characterActivityIntent = Intent(this@MainActivity, CharacterDetailsActivity::class.java)
                characterActivityIntent.putExtra("Character_ID", characterID)
                startActivity(characterActivityIntent)
            }
        }
    }

    private fun handleSearchEditText() {
        println("HII 1")
        searchCharactersEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                println("HII 2")
                searchForCharacter(s as CharSequence)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun searchForCharacter(searchName: CharSequence) {
        if (searchCharactersEditText.text != null && searchCharactersEditText.text.trim().toString().isNotEmpty()) {
            //println("HII 3")
            mainActivityViewModel.handleSearchCharacter(searchName)
        } else {
            //println("HII 4")
            mainActivityViewModel.callNetworkManager()
        }
    }
}
