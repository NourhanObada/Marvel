package com.marvel.itworx.marvel.data

import android.media.Image

data class Character (val characterID: Int,
                      val characterName: String,
                      val characterThumbnailPath: String,
                      val characterThumbnailExtension: String,
                      val characterDescription: String)