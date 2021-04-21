package com.example.postcard.ui.card.model

import java.io.Serializable

data class CardModel(val name: String, val title: String, val text: String, val imageId: Int, val profileImageId: Any) : Serializable