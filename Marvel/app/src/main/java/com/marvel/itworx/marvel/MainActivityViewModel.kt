package com.marvel.itworx.marvel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.marvel.itworx.marvel.data.Character


class MainActivityViewModel(application: Application) : AndroidViewModel(application){

    private lateinit var networkManager: NetworkManager

    private var adapter: CharactersListAdapter = CharactersListAdapter()

    fun callNetworkManager() {
        networkManager = NetworkManager(getApplication())
        networkManager.requestListContent(
                onSuccess = {
                    adapter.updateResponseList(it)
                }
                , onError = {
            handleOnResponseError(it)
        })
    }

    fun setAdapter(): CharactersListAdapter {
        return adapter
    }

    fun handleSearchCharacter(characterName:CharSequence) {
        networkManager = NetworkManager(getApplication())
        networkManager.searchList(characterName,
                onSuccess = {
                    adapter.updateResponseList(it)
                    println(it)
                }
                , onError = {
            handleOnResponseError(it)
        })
    }

    private fun handleOnResponseError(requestError: RequestError) {
        when (requestError) {
            RequestError.OFFLINE_ERROR,
            RequestError.TIMEOUT_ERROR -> {
                Toast.makeText(getApplication(), "OFFLINE OR TIMEOUT ERROR", Toast.LENGTH_SHORT).show()
            }
            RequestError.GENERAL_ERROR -> {
                Toast.makeText(getApplication(), "NETWORK ERROR", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


