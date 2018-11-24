package com.marvel.itworx.marvel

import android.content.Context
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.marvel.itworx.marvel.data.Character
import org.json.JSONObject
import java.security.NoSuchAlgorithmException
import java.math.BigInteger
import java.security.MessageDigest


class NetworkManager(context: Context) {

    private val queue: RequestQueue = Volley.newRequestQueue(context)
    private val timeStamp = (System.currentTimeMillis() / 1000).toString()
    private val baseUrl = "https://gateway.marvel.com/v1/public/characters"

    fun requestListContent(onSuccess: (ArrayList<Character>) -> Unit, onError: (RequestError) -> Unit) {
        val (apiPublicKey, hashString) = prepareApiUrl()
        val apiURL = "$baseUrl?ts=$timeStamp&apikey=$apiPublicKey&hash=$hashString&limit=20"
        val apiJSONRequest = JsonObjectRequest(Request.Method.GET, apiURL, null, Response.Listener { response ->
            handleListSuccessResponse(response, onSuccess)
        }, Response.ErrorListener { error ->
            handleErrorResponse(error, onError)
        })
        queue.add(apiJSONRequest)
    }

    fun requestDetailContent(characterID: Int, onSuccess: (Character) -> Unit, onError: (RequestError) -> Unit) {
        val (apiPublicKey, md5HashString) = prepareApiUrl()
        val apiURL = "$baseUrl/$characterID?ts=$timeStamp&apikey=$apiPublicKey&hash=$md5HashString"
        val apiJSONRequest = JsonObjectRequest(Request.Method.GET, apiURL, null, Response.Listener { response ->
            handleDetailSuccessResponse(response, onSuccess)
        }, Response.ErrorListener { error ->
            handleErrorResponse(error, onError)
        })
        queue.add(apiJSONRequest)
    }

    fun searchList(searchName: CharSequence, onSuccess: (ArrayList<Character>) -> Unit, onError: (RequestError) -> Unit) {
        val (apiPublicKey, hashString) = prepareApiUrl()
        val apiURL = "$baseUrl?nameStartsWith=$searchName&ts=$timeStamp&apikey=$apiPublicKey&hash=$hashString&limit=20"
        val apiJSONRequest = JsonObjectRequest(Request.Method.GET, apiURL, null, Response.Listener { response ->
            handleListSuccessResponse(response, onSuccess)
        }, Response.ErrorListener { error ->
            handleErrorResponse(error, onError)
        })
        queue.add(apiJSONRequest)
    }


    private fun prepareApiUrl(): Pair<String, String> {
        val apiPublicKey = "a3ff6775c44a1cf60945311c9b6aaabe"
        val apiPrivateKey = "ea251767f6179a783db00498a3f9710f6c4c66f1"
        val stringToHash = "$timeStamp$apiPrivateKey$apiPublicKey"
        val hashString = convertStringToHash(stringToHash)
        return Pair(apiPublicKey, hashString)
    }

    private fun convertStringToHash(stringToConvert: String): String {
        var temp = stringToConvert
        var hashString = ""
        val mdEnc: MessageDigest
        try {
            mdEnc = MessageDigest.getInstance("MD5")
            mdEnc.update(temp.toByteArray(), 0, temp.length)
            temp = BigInteger(1, mdEnc.digest()).toString(16)
            while (temp.length < 32) {
                temp = "0$temp"
            }
            hashString = temp
        } catch (e1: NoSuchAlgorithmException) {
            e1.printStackTrace()
        }

        return hashString
    }

    private fun handleListSuccessResponse(response: JSONObject, onSuccess: (ArrayList<Character>) -> Unit) {
        var charactersList = ArrayList<Character>()
        val responseArray = response.getJSONObject("data").getJSONArray("results")
        for (counter in 0..(responseArray.length() - 1)) {
            val userObject = responseArray.optJSONObject(counter)
            val character = Character(userObject.getInt("id"),
                    userObject.getString("name"),
                    userObject.getJSONObject("thumbnail").getString("path"),
                    userObject.getJSONObject("thumbnail").getString("extension"),
                    userObject.getString("description"))
            charactersList.add(character)
        }
        onSuccess(charactersList)
    }

    private fun handleDetailSuccessResponse(response: JSONObject, onSuccess: (Character) -> Unit) {
        val responseArray = response.getJSONObject("data").getJSONArray("results")
        val userObject = responseArray.optJSONObject(0)
        val character = Character(userObject.getInt("id"),
                userObject.getString("name"),
                userObject.getJSONObject("thumbnail").getString("path"),
                userObject.getJSONObject("thumbnail").getString("extension"),
                userObject.getString("description"))
        onSuccess(character)
    }

    private fun handleErrorResponse(error: VolleyError?, onError: (RequestError) -> Unit) {
        when (error) {
            is NetworkError -> onError(RequestError.OFFLINE_ERROR)
            is TimeoutError -> onError(RequestError.TIMEOUT_ERROR)
            else -> onError(RequestError.GENERAL_ERROR)
        }
    }
}

enum class RequestError {
    OFFLINE_ERROR,
    TIMEOUT_ERROR,
    GENERAL_ERROR
}

