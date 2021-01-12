package com.example.smartmanager.activity
import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener

open class OnSwipeTouchListener/**
 * Instantiates a new on swipe touch listener.
 *
 * @param context
 * the context
 */(context: Context):OnTouchListener {
    /**
     * Gets the gesture detector.
     *
     * @return the gesture detector
     */
    val gestureDetector:GestureDetector
    private val context:Context
    /* (non-Javadoc)
  * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
  */    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    override fun onTouch(view:View, motionEvent:MotionEvent):Boolean {
        return gestureDetector.onTouchEvent(motionEvent)
    }
    init{
        this.context = context
        gestureDetector = GestureDetector(context, GestureListener())
    }
    private inner class GestureListener:SimpleOnGestureListener() {
        /* (non-Javadoc)
     * @see android.view.GestureDetector.SimpleOnGestureListener#onDown(android.view.MotionEvent)
     */
        override fun onDown(e:MotionEvent):Boolean {
            return true
        }
        /* (non-Javadoc)
     * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
     */
        override fun onFling(e1:MotionEvent, e2:MotionEvent, velocityX:Float, velocityY:Float):Boolean {
            val result = false
            try
            {
                val diffY = e2.getRawY() - e1.getRawY()
                val diffX = e2.getRawX() - e1.getRawX()
                if ((Math.abs(diffX) - Math.abs(diffY)) > SWIPE_THRESHOLD)
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffX > 0)
                        {
                            onSwipeRight()
                        }
                        else
                        {
                            onSwipeLeft()
                        }
                    }
                }
                else
                {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffY > 0)
                        {
                            onSwipeBottom()
                        }
                        else
                        {
                            onSwipeTop()
                        }
                    }
                }
            }
            catch (e:Exception) {
            }
            return result
        }
    }
    /**
     * On swipe right.
     */
    open fun onSwipeRight() {}
    /**
     * On swipe left.
     */
    open fun onSwipeLeft() {}
    /**
     * On swipe top.
     */
    open fun onSwipeTop() {}
    /**
     * On swipe bottom.
     */
    open fun onSwipeBottom() {}
}