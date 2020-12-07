package com.example.musicapp

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.music_player.*


class MusicPlayer : AppCompatActivity() {


    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable:Runnable
    private var handler: Handler = Handler()
    private var pause:Boolean = false
    private var songpath:ArrayList<String> = ArrayList()
    private var songname: ArrayList<String> = ArrayList()
    private var albumart: ArrayList<String> = ArrayList()
    private var pos:Int = 0
    private lateinit var i: Intent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.music_player)
        i = intent

        Log.d("onCreate","called")

    }

    override fun onResume() {
        super.onResume()

        Log.d("onResume","called")

        songpath = i.getStringArrayListExtra("songpath")!!
        songname = i.getStringArrayListExtra("songname")!!
        pos = i.getIntExtra("position",0)
        albumart = i.getStringArrayListExtra("albumart")!!

        Log.d("songpath onResume", songpath.get(pos))

        imageView.setImageURI(getAlbumArtUri(albumart.get(pos).toLong()))
        songname_tv.setText(songname.get(pos))
        mediaPlayer = MediaPlayer.create(this, Uri.parse(songpath.get(pos)))

        mediaplay()

    }


    fun getAlbumArtUri(albumId: Long): Uri? {
        return ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
        )
    }

    fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SdCardPath")
    public fun mediaplay()
    {
        Log.d("mediaplay","called")

        initializeSeekBar()

        // Start the media player
        playBtn.setOnClickListener{

            Log.d("play song","called")

            playBtn.visibility= View.INVISIBLE
            pauseBtn.visibility = View.VISIBLE

            if(pause){

                Log.d("play song if pause","called")

                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false

            }

            else{

                Log.d("play song if play","called")


                mediaPlayer = MediaPlayer.create(this, Uri.parse(songpath.get(pos)));
                mediaPlayer.setLooping(true);
                mediaPlayer.start()

                pause = false


            }

            mediaPlayer.setOnCompletionListener {

                playBtn.visibility= View.VISIBLE
                pauseBtn.visibility = View.INVISIBLE

                Log.d("song ends","called")
            }
        }
        // Pause the media player
        pauseBtn.setOnClickListener {

            Log.d("pause song","called")

            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                pause = true

                playBtn.visibility= View.VISIBLE
                pauseBtn.visibility = View.INVISIBLE
//                stopBtn.isEnabled = true
            }
        }

        backward.setOnClickListener{

            Log.d("backward song","called")

            if(pos>=1 && pos<songname.size) {
                --pos
                mediaPlayer.stop()
                mediaPlayer.reset()
                changeSong()
            }
            else {
                showToast("This is the first song")
                pos=0
            }
        }

        forward.setOnClickListener{

            Log.d("forward song","called")

            if(pos>=0 && pos<songname.size-1) {
                ++pos
                mediaPlayer.stop()
                mediaPlayer.reset()
                changeSong()
            }

            else {
                showToast("This was the last song.")
                pos=songname.size-1
            }

        }

        // Stop the media player
      /*  stopBtn.setOnClickListener{
            if(mediaPlayer.isPlaying || pause.equals(true)){
                pause = false
                seek_bar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()
                handler.removeCallbacks(runnable)

                playBtn.isEnabled = true
                pauseBtn.isEnabled = false
                stopBtn.isEnabled = false
                tv_pass.text = ""
                tv_due.text = ""
                Toast.makeText(this,"media stop",Toast.LENGTH_SHORT).show()
            }
        }*/

    }

    fun changeSong()
    {

        Log.d("change song","called")

        imageView.setImageURI(getAlbumArtUri(albumart.get(pos).toLong()))
        songname_tv.setText(songname.get(pos))
        mediaPlayer = MediaPlayer.create(this, Uri.parse(songpath.get(pos)))
        mediaPlayer.start()
        playBtn.visibility=View.INVISIBLE
        pauseBtn.visibility=View.VISIBLE

    }

    @SuppressLint("SetTextI18n")
    private fun initializeSeekBar() {

        Log.d("Initialise seek bar","called")

        seek_bar.max = mediaPlayer.seconds

        runnable = Runnable {
            seek_bar.progress = mediaPlayer.currentSeconds

            start.text = "${mediaPlayer.currentSeconds/60}:${mediaPlayer.currentSeconds%60}"
//            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            end.text = "${mediaPlayer.seconds/60}:${mediaPlayer.seconds%60}"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)

        // Seek bar change listener
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                    pause = true
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    // Creating an extension property to get the media player time duration in seconds
    val MediaPlayer.seconds:Int
        get() {
            return this.duration / 1000
        }
    // Creating an extension property to get media player current position in seconds
    val MediaPlayer.currentSeconds:Int
        get() {
            return this.currentPosition/1000
        }


    override fun onStop() {
        super.onStop()

        Log.d("onStop","called")

        if(mediaPlayer.isPlaying || pause.equals(true)){
            pause = false
            seek_bar.setProgress(0)
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
            handler.removeCallbacks(runnable)

            playBtn.visibility= View.VISIBLE
            pauseBtn.visibility = View.INVISIBLE
//          stopBtn.isEnabled = false

        }

    }

}