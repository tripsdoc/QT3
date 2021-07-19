package com.hsc.qda.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.hsc.qda.MainActivity
import com.hsc.qda.data.model.quupa.position.TagPositionResponse
import com.hsc.qda.data.network.QuupaClient
import com.hsc.qda.listener.OnFinishedSingleRequest
import com.hsc.qda.ui.retrieve.RetrieveActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class QuupaPositioningEngine(context: Context) {

    private var activity: RetrieveActivity = context as RetrieveActivity
    private var quupaClient = QuupaClient(activity.dataAddress)

    companion object {
        private lateinit var listener: OnFinishedSingleRequest
        fun bindListener(listener: OnFinishedSingleRequest) {
            this.listener = listener
        }
    }

    fun observePositions() {
        activity.r = 0
        activity.l1 = 0
        activity.l2 = 0
        activity.s = 0
        activity.disposable = Observable.interval(activity.timeDelay, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::callQpeAPI, this::onErrorQpeAPI)
    }

    @SuppressLint("CheckResult")
    private fun callQpeAPI(long: Long) {
        activity.r += 1
        //activity.updateText()
        val observe: Observable<TagPositionResponse>? = quupaClient.getTagPosition(
            activity.availableTags
        )
    }

    private fun onErrorQpeAPI(t: Throwable) {
        activity.l1 += 1
        //activity.updateText()
        t.printStackTrace()
        Log.e("ObservePosition", "Network Problem")
    }

    private fun errorHandler(t: Throwable) {
        activity.l2 += 1
        //activity.updateText()
        t.printStackTrace()
        Log.e("ObservePosition", "Network Problem")
    }

    private var timerNow: Long = 0

    private fun startTimer() {
        timerNow = System.currentTimeMillis()
    }

    private fun stopTimer() {
        val endTime = System.currentTimeMillis()
        val diff = endTime - timerNow
        listener.onFinishedSingleRequest(timerNow, endTime)
        val response = "Start at : ${getDate(timerNow)}, End at : ${getDate(endTime)}  (${diff}ms)"
        ResponseWriter.writeResponse(response, "response.txt")
        //activity.dataResponse.add(response)
    }

    private fun getDate(time:Long):String {
        val date = Date(time)
        val sdf = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss.SSS", Locale.US)

        return sdf.format(date);
    }
}