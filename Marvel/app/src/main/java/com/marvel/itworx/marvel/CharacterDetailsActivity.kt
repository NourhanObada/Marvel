package com.marvel.itworx.marvel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.marvel.itworx.marvel.data.Character
import kotlinx.android.synthetic.main.character_detail.*

class CharacterDetailsActivity: AppCompatActivity(){

    lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.character_detail)
        networkManager = NetworkManager(this)
        var characterID = 0
        characterID = getCharacterId(savedInstanceState, characterID)
        prepareNetworkManager(characterID)
    }

    private fun prepareNetworkManager(characterID: Int) {
        networkManager.requestDetailContent(characterID,
                onSuccess = {
                    handleOnSuccess(it)
                }, onError = {
            handleOnError(it)
        })
    }

    private fun getCharacterId(savedInstanceState: Bundle?, characterID: Int): Int {
        var characterID1 = characterID
        if (savedInstanceState == null) {
            val extras = intent.extras
            characterID1 = extras.getInt("Character_ID")
        } else {
            characterID1 = savedInstanceState.getSerializable("Character_ID") as Int
        }
        return characterID1
    }

    private fun handleOnSuccess(response: Character) {
        characterDetailName.text = response.characterName
        characterDetailDescription.text = response.characterDescription
        var context = characterDetailImage.context
        var characterImagePath = "${response.characterThumbnailPath}/portrait_small.${response.characterThumbnailExtension}"
        if (characterImagePath != null) {
            Glide.with(context)
                    .load(characterImagePath)
                    .into(characterDetailImage)
        }
    }

    private fun handleOnError(requestError:RequestError){

    }
}

