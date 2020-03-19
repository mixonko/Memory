package com.mixonko.android.game

import android.content.Context
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import java.text.DecimalFormat

class Chronometer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextView(context, attrs, defStyle) {

    private var mBase: Long = 0
    private var mVisible: Boolean = false
    private var mStarted: Boolean = false
    private var mRunning: Boolean = false
    var onChronometerTickListener: OnChronometerTickListener? = null

    var timeElapsed: Long = 0
        private set
    var base: Long
        get() = mBase
        set(base) {
            mBase = base
            dispatchChronometerTick()
            updateText(SystemClock.elapsedRealtime())
        }

    private val mHandler = object : Handler() {
        override fun handleMessage(m: Message) {
            if (mRunning) {
                updateText(SystemClock.elapsedRealtime() )
                dispatchChronometerTick()
                sendMessageDelayed(
                    Message.obtain(this, TICK_WHAT),
                    10
                )
            }
        }
    }

    interface OnChronometerTickListener {

        fun onChronometerTick(chronometer: Chronometer)
    }

    init {
        init()
    }

    private fun init() {
        mBase = SystemClock.elapsedRealtime()
        updateText(mBase)
        setText("00:00")

    }

    fun start() {
        mStarted = true
        updateRunning()
    }

    fun stop() {
        mStarted = false
        updateRunning()
    }

    fun isRunning() = mRunning

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mVisible = false
        updateRunning()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        mVisible = visibility == View.VISIBLE
        updateRunning()
    }

    @Synchronized
    private fun updateText(now: Long) {
        timeElapsed = now - mBase

        val df = DecimalFormat("00")

        val hours = (timeElapsed / (3600 * 1000)).toInt()
        var remaining = (timeElapsed % (3600 * 1000)).toInt()

        val minutes = remaining / (60 * 1000)
        remaining = remaining % (60 * 1000)

        val seconds = remaining / 1000
        remaining = remaining % 1000

        val milliseconds = timeElapsed.toInt() % 1000 / 10

        var text = ""

        if (hours > 0) {
            text += df.format(hours) + ":"
        }

        if (minutes!= 0)text += df.format(minutes) + ":"
        text += df.format(seconds) + ":"
        var textMillis = milliseconds.toString()
        if (textMillis.length == 1) {textMillis += "0"}
        text += textMillis
        setText(text)
    }

    private fun updateRunning() {
        val running = mVisible && mStarted
        if (running != mRunning) {
            if (running) {
                updateText(SystemClock.elapsedRealtime())
                dispatchChronometerTick()
                mHandler.sendMessageDelayed(
                    Message.obtain(
                        mHandler,
                        TICK_WHAT
                    ), 10
                )
            } else {
                mHandler.removeMessages(TICK_WHAT)
            }
            mRunning = running
        }
    }

    internal fun dispatchChronometerTick() {
        if (onChronometerTickListener != null) {
            onChronometerTickListener!!.onChronometerTick(this)
        }
    }

    companion object {
        private val TICK_WHAT = 2
    }

}
