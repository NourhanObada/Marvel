package com.marvel.itworx.marvel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.marvel.itworx.marvel.data.Character


class MainActivityViewModel(application: Application) : AndroidViewModel(application){

    private lateinit var networkManager: NetworkManager

    fun callNetworkManager() {
        networkManager = NetworkManager(getApplication())
        networkManager.requestListContent(
                onSuccess = {

                }
                , onError = {
        })
    }

}