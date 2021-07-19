package com.hsc.qda.ui.retrieve

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.hsc.qda.R
import com.hsc.qda.data.model.quupa.position.Tag
import com.hsc.qda.data.model.quupa.qda.QdaPositionResponse
import com.hsc.qda.data.model.retrieve.RetrieveResponse
import com.hsc.qda.data.network.NetworkClient
import com.hsc.qda.data.network.QuupaClient
import com.hsc.qda.listener.OnFinishedSingleRequest
import com.hsc.qda.listener.OnWifiChanged
import com.hsc.qda.utilities.*
import com.hsc.qda.utilities.maps.RoomSpecs
import com.hsc.qda.utilities.tileView.TileView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_retrieve.*
import kotlinx.android.synthetic.main.tag_marker.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrieveActivity : AppCompatActivity(), OnWifiChanged, OnFinishedSingleRequest {

    private lateinit var networkClient: NetworkClient
    private lateinit var userData: UserPreferences
    private lateinit var progressDialog: ProgressDialog

    private lateinit var tileView: TileView
    private lateinit var warehouses: String
    private lateinit var currentWarehouse: String
    private lateinit var arrayTags: ArrayList<String>
    private lateinit var arrayHBL: ArrayList<String>
    private lateinit var arrayDN: ArrayList<String>
    private lateinit var mainHandler : Handler
    private lateinit var specs: RoomSpecs

    private lateinit var tagViewList: HashMap<String, View>
    private lateinit var callOutTagList: HashMap<String, String>
    private lateinit var userTag: String
    private var isTabConfigured = false
    private var mode: Int = 0
    lateinit var dataAddress: String
    lateinit var availableTags: String
    var r = 0
    var l1 = 0
    var l2 = 0
    var s = 0
    var timeDelay: Long = 100
    var disposable: Disposable? = null
    var isNotFromFullMap = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrieve)
        WifiReceiver.bindListener(this)
        userData = UserPreferences(this)
        userTag = userData.getTag(UserPreferences.AVAILABLE_TAG).toString()
        tagViewList = HashMap()
        callOutTagList = HashMap()

        arrayHBL = ArrayList()
        arrayDN = ArrayList()
        arrayTags = ArrayList()

        mainHandler = Handler(Looper.getMainLooper())

        progressDialog = ProgressDialog(this)

        warehouses = "111"
        currentWarehouse = "111"
        setupView()

        refreshBtn.setOnClickListener {
            callTagList()
        }
    }

    private val updateTagData = object: Runnable{
        override fun run() {
            isNotFromFullMap = true
            callTagList()
            mainHandler.postDelayed(this, 1000 * 120)
        }
    }

    private fun setupView() {
        configureRefresh()
        changeWarehouse()
    }

    private fun changeWarehouse() {
        tileViewMapLayout.removeAllViews()

        tagViewList.clear()
        arrayHBL.clear()
        arrayTags.clear()

        currentWarehouse = "111"

        val imgMapPath = "map_01_$currentWarehouse/"
        val filename = "%d_%d.jpg"

        specs = RoomSpecs().getRoomSpec(currentWarehouse)
        tileView = TileView(this)
        tileView.apply {
            setSize(specs.imageWidth, specs.imageHeight)
            addDetailLevel(1F, imgMapPath + "125/" + filename, 256, 256)
            addDetailLevel(0.5F, imgMapPath + "250/" + filename, 128, 128)
            defineBounds(specs.west, specs.north, specs.east, specs.south)
            setScaleLimits(0F, 7F)
            scale = 0.5F
            setShouldRenderWhilePanning(true)
        }

        tileView.setViewportPadding(256)
        tileViewMapLayout.addView(tileView)
    }

    fun updateQdaPosition(data: List<QdaPositionResponse>) {
        s += 1
        for (tag in data) {
            if (tag.areaName.contains("02-111")) {
                if (tagViewList.contains(tag.id)) {
                    tileView.moveMarker(
                        tagViewList[tag.id],
                        tag.smoothedPositionY,
                        tag.smoothedPositionX
                    )
                } else {
                    if (userTag.contains(tag.id)) {
                        tagViewList[tag.id] = addTagForklift(
                            tag.id,
                            tag.smoothedPositionY,
                            tag.smoothedPositionX
                        )
                    } else {
                        tagViewList[tag.id] = addTagMarker(
                            tag.id,
                            tag.smoothedPositionY,
                            tag.smoothedPositionX
                        )
                    }
                }
            } else {
                tileView.removeMarker(tagViewList[tag.id])
                tagViewList.remove(tag.id)
            }
        }
    }

    private fun addTagForklift(tagId: String, x: Double, y: Double) : View {
        val marker = ImageView(this)
        marker.tag = tagId
        marker.setImageResource(R.drawable.forklift)
        return tileView.addMarker(marker, x, y, -0.5F, -1.0F, 0F, 0F)
    }

    @SuppressLint("InflateParams")
    private fun addTagMarker(tagId: String, x: Double, y: Double): View {
        try {
            val tagView = LayoutInflater.from(this).inflate(R.layout.tag_marker, null, false)
            val index = arrayTags.indexOf(tagId)
            Log.d("Array", arrayDN.joinToString(","))
            if (index != -1) {
                val dn = arrayDN[index]
                if (index < arrayDN.size) {
                    tagView.tagText.text = dn
                }
            }
            return tileView.addLayoutMarker(tagView, x, y, -0.5F, -1.0F, 0F, 0F)
        } catch (e: Exception) {
            Log.d("Data", "Error Marker : $tagId -> ${e.message}")
            return View(this)
        }
    }

    private fun configureRefresh() {
        refreshBtn.setOnClickListener {
            cancelRequest()
        }
    }

    private fun refreshTag() {
        availableTags = ""
        disposable?.dispose()
        cancelRequest()
        removeMarkers()
        tagViewList.clear()
        arrayTags.clear()
        arrayHBL.clear()
        arrayDN.clear()
    }

    private fun callTagList() {
        refreshTag()
        networkClient.retrieveTagList()
            ?.enqueue(object : Callback<RetrieveResponse> {
                override fun onFailure(call: Call<RetrieveResponse>, t: Throwable) {
                    Log.d("Debug", t.message.toString())
                }
                override fun onResponse(
                    call: Call<RetrieveResponse>,
                    response: Response<RetrieveResponse>
                ) {
                    processTagData(response)
                }
            })
    }

    private fun processTagData() {
        QuupaDataAggregator(this).observePositions()
    }

    private fun processTagData(response: Response<RetrieveResponse>) {
        if (response.isSuccessful) {
            if (response.body()!!.status) {
                for (tag in response.body()!!.data) {
                    arrayTags.add(tag.Tag)
                    arrayDN.add(tag.DN)
                }
                availableTags = arrayTags.joinToString(",")
                Log.d("Debug Tag", arrayTags.joinToString(","))
                processingTags()
            } else {
                removeMarkers()
                processingTags()
            }
        }
    }

    private fun processingTags() {
        if(userData.getTag(UserPreferences.AVAILABLE_TAG) != "") {
            if (availableTags != "" && arrayTags.size > 0) {
                availableTags = "$availableTags,$userTag"
            }
        }
        processTagData()
    }

    private fun removeMarkers() {
        for(view in tagViewList.values) {
            tileView.removeMarker(view)
        }
        tagViewList.clear()
        tagViewList = HashMap()
    }

    private fun initializeIpAddress() {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ip = wifiInfo.ipAddress
        val ipAddress = String.format(
            "%d.%d.%d.",
            ip and 0xff,
            ip shr 8 and 0xff,
            ip shr 16 and 0xff
        )
        if (QuupaClient.QPA_URL.contains(ipAddress)) {
            mode = 0
            dataAddress = QuupaClient.QDA_URL
            networkClient = NetworkClient(NetworkClient.BASE_URL)
        } else {
            mode = 1
            dataAddress = QuupaClient.QT3_URL
            networkClient = NetworkClient(NetworkClient.BASE_QT3)
        }
        exportLabel.text = "Retrieve (Mode : $mode)"
        mainHandler.post(updateTagData)
    }

    override fun onStart() {
        super.onStart()
        isTabConfigured = true
    }

    override fun onResume() {
        super.onResume()
        initializeIpAddress()
    }

    override fun onPause() {
        super.onPause()
        cancelRequest()
        disposable?.dispose()
        mainHandler.removeCallbacks(updateTagData)
    }

    private fun cancelRequest() {
        networkClient.cancelAllRequest()
    }

    override fun onFinishedSingleRequest(start: Long, end: Long) {

    }

    override fun onWifiChanged(ipAddress: String) {
        if (QuupaClient.QPA_URL.contains(ipAddress)) {
            mode = 0
            dataAddress = QuupaClient.QDA_URL
            networkClient = NetworkClient(NetworkClient.BASE_URL)
        } else {
            mode = 1
            dataAddress = QuupaClient.QT3_URL
            networkClient = NetworkClient(NetworkClient.BASE_QT3)
        }
        exportLabel.text = "Retrieve (Mode : $mode)"
        callTagList()
    }
}