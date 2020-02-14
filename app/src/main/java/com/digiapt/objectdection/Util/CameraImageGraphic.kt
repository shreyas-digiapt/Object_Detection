package com.digiapt.objectdection.Util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import com.digiapt.objectdection.GraphicsOverlay

class CameraImageGraphic(overlay: GraphicsOverlay, private var bitmap: Bitmap) : GraphicsOverlay.Graphic(overlay) {

    override fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmap, null,Rect(0,0, canvas.width, canvas.height), null)
    }

}