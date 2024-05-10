package com.example.videoplayertutorial

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayertutorial.data.HomeScreenUiState
import com.example.videoplayertutorial.data.client
import com.example.videoplayertutorial.model.Videos
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.filter.TextSearchType
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.ktor.util.Identity.decode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class VideoPlayerViewModel:ViewModel() {
    val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState : StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    val searching = mutableStateOf(false)
    var searchString = mutableStateOf("")

    fun onSearchValueChanged(newString:String){
        searchString.value = newString
    }
    fun onSearchButtonClicked(){
        searching.value = !searching.value
    }
    fun clearSearchString(){
        searchString.value = ""
    }
    suspend fun onSearchClick():PostgrestResult{
        val result = client.supabase.from("videos_table").select(columns = Columns.list()){
            filter {
                or {
                    textSearch(column = "name" , query = searchString.value.plus(".mp4"), config = "english", textSearchType = TextSearchType.NONE)
                    textSearch(column = "channel_name" , query = searchString.value, config = "english", textSearchType = TextSearchType.NONE)
                }
            }
        }

        return result
    }

    init {

            viewModelScope.launch {
                val data = client.supabase.from("videos_table").select()
                _homeScreenUiState.update { currenState -> currenState.copy(
                    listOfVideos = data.decodeList<Videos>()
                ) }

            }

            viewModelScope.launch {
                while(true){
                    if(searching.value){
                        var data = onSearchClick()

                        if (data.data != "[]"){
                            Log.d("Supabase", data.data)
                            _homeScreenUiState.update { currentState -> currentState.copy(
                                listOfVideos = data.decodeList<Videos>()
                            ) }
                        }



                    }
                    else if(searching.value == false){
                        val data = client.supabase.from("videos_table").select()
                        _homeScreenUiState.update { currenState -> currenState.copy(
                            listOfVideos = data.decodeList<Videos>()
                        ) }
                    }
                    sleep(1000)
                }

            }



    }
}