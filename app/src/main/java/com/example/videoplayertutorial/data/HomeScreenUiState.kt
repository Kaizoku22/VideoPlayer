package com.example.videoplayertutorial.data

import com.example.videoplayertutorial.model.Videos

data class HomeScreenUiState(
    val listOfVideos:List<Videos> = emptyList()
)
