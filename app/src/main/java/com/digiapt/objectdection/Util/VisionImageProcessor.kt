package com.digiapt.objectdection.Util

import android.graphics.Bitmap
import com.digiapt.objectdection.GraphicsOverlay
import java.nio.ByteBuffer

interface VisionImageProcessor  {

    fun process(data: ByteBuffer, frameMetaData: FrameMetaData, overlay: GraphicsOverlay)

    fun process(bitmap: Bitmap, overlay: GraphicsOverlay)

    fun stop()
}