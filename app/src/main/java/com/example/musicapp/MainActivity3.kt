package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main3.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity3 : AppCompatActivity(),ListAdapter.OnItemClickListener  {

    var adapter: ArrayAdapter<String>? = null

    private val songlist: ArrayList<String> = ArrayList()
    private val songlist_copy=ArrayList<String>()
    private val songpath:ArrayList<String> = ArrayList()
    private val songpath_copy = ArrayList<String>()
    private val albumart:ArrayList<String> = ArrayList()
    private val albumart_copy = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)


        fetchmp3()

        Log.d("oncreate","prints")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        Log.d("oncreate options menu","prints")
        menuInflater.inflate(R.menu.search_item, menu)

        val menuitem = menu?.findItem(R.id.search)

        if(menuitem!=null) {
            val searchView = menuitem.actionView as SearchView

            searchView.inputType = InputType.TYPE_CLASS_TEXT
            searchView.queryHint = "Search for a song"
            searchView.setIconifiedByDefault(true)

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText!!.isNotEmpty()) {
                        songlist_copy.clear()
                        songpath_copy.clear()
                        albumart_copy.clear()

                        val search = newText.toLowerCase(Locale.getDefault())

                        for (i in 0..songlist.size-1) {

                            val item = songlist[i]
                            if (item.toLowerCase(Locale.getDefault()).contains(search)) {
                                songlist_copy.add(item)
                                songpath_copy.add(songpath[i])
                                albumart_copy.add(albumart[i])
                            }

                        }

                        recyclerView.adapter!!.notifyDataSetChanged()

                    } else {
                        songlist_copy.clear()
                        songpath_copy.clear()
                        albumart_copy.clear()
                        songlist_copy.addAll(songlist)
                        songpath_copy.addAll(songpath)
                        albumart_copy.addAll(albumart)

                        recyclerView.adapter!!.notifyDataSetChanged()
                    }

                    return true
                }
            })
        }

        return super.onCreateOptionsMenu(menu)

    }


    fun fetchmp3() {

        Log.d("fetchmp3","prints")
        val cr = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"
        val projection = arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Albums.ALBUM_ID
        )
        val cur = cr.query(uri, projection, selection, null, sortOrder)
        var count = 0
        if (cur != null) {
            count = cur.count
            if (count > 0) {
                while (cur.moveToNext()) {
                    songlist.add(cur.getString(0))
                    songpath.add(cur.getString(1))
                    albumart.add(cur.getString(2))
                }
            }
            cur.close()
        }
        songlist_copy.addAll(songlist)
        songpath_copy.addAll(songpath)
        albumart_copy.addAll(albumart)

        recyclerView.adapter = ListAdapter(songlist_copy, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

    }

    override fun onItemClick(position: Int) {

        val intent = Intent(this@MainActivity3, MusicPlayer::class.java)
        intent.putExtra("songpath", songpath_copy)
        intent.putExtra("position",position)
        intent.putExtra("songname", songlist_copy)
        intent.putExtra("albumart", albumart_copy)
        startActivity(intent)

        adapter?.notifyDataSetChanged()

    }
}

