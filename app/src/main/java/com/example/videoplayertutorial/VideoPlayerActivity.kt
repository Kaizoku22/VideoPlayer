package com.example.videoplayertutorial

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import com.example.videoplayertutorial.ui.theme.VideoPlayerTutorialTheme
import io.github.jan.supabase.storage.storage
import com.example.videoplayertutorial.data.client

val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()
@UnstableApi
var player : ExoPlayer? = null

class VideoPlayerActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoName = intent.getStringExtra("videoTitle")

        setContent {
            VideoPlayerTutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mediaUrl = client.videoBucket.publicUrl(videoName!!)

                    val playerListener = object : Player.Listener {
                        override fun onPlaybackStateChanged(playbackState: Int) {
                            val stateString: String = when (playbackState) {
                                ExoPlayer.STATE_IDLE -> "ExoPlayer.STATE_IDLE      -"
                                ExoPlayer.STATE_BUFFERING -> "ExoPlayer.STATE_BUFFERING -"
                                ExoPlayer.STATE_READY -> "ExoPlayer.STATE_READY     -"
                                ExoPlayer.STATE_ENDED -> "ExoPlayer.STATE_ENDED     -"
                                else -> "UNKNOWN_STATE             -"
                            }
                            Log.d(ContentValues.TAG, "changed state to $stateString")
                        }
                    }

                    player = ExoPlayer.Builder(this)
                        .build()
                        .apply {

                            val source = if (mediaUrl.contains("m3u8"))
                                HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                                    MediaItem.fromUri(mediaUrl))
                            else
                                ProgressiveMediaSource.Factory(dataSourceFactory)
                                    .createMediaSource(MediaItem.fromUri(mediaUrl))

                            setMediaSource(source)
                            prepare()
                            addListener(playerListener)
                        }
                   Column {
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
                       TF(player = player!!)
                   }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun TF(player: ExoPlayer){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ){
        AndroidView(factory = { context ->
            PlayerView(context).also{
                it.player = player
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
        )
        
        Surface(modifier = Modifier
            .height(600.dp)
            .fillMaxWidth(),
            color = Color.Gray) {

            Surface(
                modifier = Modifier
                    .height(600.dp)
                    .fillMaxWidth()
                    .padding(8.dp),

                shape = RoundedCornerShape(5.dp),
                color = Color.DarkGray
            ) {
                Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "comments",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Red)
                }

            }
        }
    }

}