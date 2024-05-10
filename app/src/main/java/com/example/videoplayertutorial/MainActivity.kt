package com.example.videoplayertutorial

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.media3.common.util.UnstableApi
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.compose.AsyncImage
import com.example.videoplayertutorial.data.client
import com.example.videoplayertutorial.model.Videos
import com.example.videoplayertutorial.ui.theme.VideoPlayerTutorialTheme
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.coil.coil
import kotlinx.serialization.json.jsonObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class MainActivity : ComponentActivity(), ImageLoaderFactory {
    @kotlin.OptIn(SupabaseExperimental::class)
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .components {
                add(client.supabase.coil)
            }
            .build()
    }

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val  videoPlayerViewModel = VideoPlayerViewModel()


        setContent {
            VideoPlayerTutorialTheme {
                val  homeScreenUiState by videoPlayerViewModel.homeScreenUiState.collectAsState()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        
                        Row(

                        ) {
                            Surface(
                                modifier = Modifier
                                    .height(50.dp)
                                    .fillMaxWidth(),

                                color = Color.Red) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center){
                                    Text(
                                        text = "VideoPlayer",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = Color.White)
                                }

                            }
                        }
                        
                        Row(modifier = Modifier
                            .padding(16.dp)
                            .height(60.dp)) {
                            TextField(
                                modifier = Modifier
                                    .weight(1.9f)
                                    .fillMaxHeight(),
                                shape = RoundedCornerShape(20),
                                value = videoPlayerViewModel.searchString.value,
                                onValueChange ={ newValue -> videoPlayerViewModel.onSearchValueChanged(newValue)} )
                            Button(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                                    .fillMaxHeight(),
                                shape = RoundedCornerShape(20),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                                onClick = {
                                if(videoPlayerViewModel.searching.value == true){
                                    videoPlayerViewModel.clearSearchString()
                                }
                                videoPlayerViewModel.onSearchButtonClicked()

                            }) {
                                if(videoPlayerViewModel.searching.value){
                                    Text(text = "Clear")
                                }
                                else{
                                    Text(text = "Search")
                                }
                            }
                        }
                        LazyColumn{
                            items(homeScreenUiState.listOfVideos){
                                videoCard(item = it, context = this@MainActivity)
                            }

                        }
                    }

                }
            }
        }

    }
}




@Composable
fun videoCard(item:Videos,context:Context){




    Card(modifier = Modifier
        .padding(16.dp)
        .clickable {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            intent.putExtra("videoTitle", item.name)
            startActivity(context, intent, null)
        }) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(modifier = Modifier.padding(bottom = 4.dp, start = 12.dp),
                text = item.name.dropLast(4),
                style = MaterialTheme.typography.titleLarge)

        }

        AsyncImage(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .aspectRatio(1280f / 847f),
            model = client.videoBucket.publicUrl("thumbnails/${item.thumbnail}"), //for public buckets
            contentDescription = null,
        )
        Row(modifier = Modifier.padding(start = 16.dp)) {
            Text(text = item.channel_name)
        }
        Row(modifier = Modifier.padding(start = 16.dp)) {
           Text(text = "Likes:")
           Text(text = item.likes.toString())
            Icon(
                Icons.Filled.Favorite,
                contentDescription = "Likes"
            )
            
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "Posted on:")
            Text(text = item.created_at.toString().substring(1,11))
        }
        
    }
}
