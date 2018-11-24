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

    private lateinit var networkManager: NetworkManager
    private lateinit var recyclerViewManager: RecyclerView.LayoutManager
    //private lateinit var mainActivityViewModel:MainActivityViewModel
    //private lateinit var responseCharactersList:ArrayList<Character>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //mainActivityViewModel = MainActivityViewModel(application)
        //mainActivityViewModel.callNetworkManager()
        networkManager = NetworkManager(this)
        recyclerViewManager = LinearLayoutManager(this)
        callNetworkManager()
        handleSearchEditText()

    }

    private fun callNetworkManager() {
        networkManager.requestListContent(
                onSuccess = {
                    handleOnSuccess(it)
                }, onError = {
            handleOnError(it)
        })
    }

    private fun handleSearchEditText() {
        searchCharactersEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
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
            networkManager.searchList(searchName,
                    onSuccess = {
                        handleOnSuccess(it)
                    }, onError = {
                handleOnError(it)
            })
        }else {
            callNetworkManager()
        }
    }


    private fun handleOnSuccess(charactersList: ArrayList<Character>) {
        charactersRecyclerView.layoutManager = recyclerViewManager
        val characterRecyclerViewAdapter = CharactersListAdapter(charactersList)
        characterRecyclerViewAdapter.characterListRecyclerViewAdapterListener = object : CharacterListRecyclerViewAdapterListener {
            override fun onItemClick(characterID: Int) {
                val characterActivityIntent = Intent(this@MainActivity, CharacterDetailsActivity::class.java)
                characterActivityIntent.putExtra("Character_ID", characterID)
                startActivity(characterActivityIntent)
            }
        }
        charactersRecyclerView.adapter = characterRecyclerViewAdapter
        charactersRecyclerView.visibility = View.VISIBLE
    }

    private fun handleOnError(requestError: RequestError) {
        when (requestError) {
            RequestError.OFFLINE_ERROR,
            RequestError.TIMEOUT_ERROR -> {
                Toast.makeText(this, "OFFLINE OR TIMEOUT ERROR", Toast.LENGTH_SHORT).show()
            }
            RequestError.GENERAL_ERROR -> {
                Toast.makeText(this, "NETWORK ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



