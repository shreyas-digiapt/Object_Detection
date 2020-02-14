package com.digiapt.objectdection

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.digiapt.objectdection.Util.ObjectInterface
import com.google.firebase.ml.vision.objects.FirebaseVisionObject

class ObjectGraphics internal constructor(
    context: Context,
     overlay:GraphicsOverlay,
     private val visionObject: FirebaseVisionObject
):GraphicsOverlay.Graphic(overlay) {

    private val boxPaint: Paint
    private val textPaint: Paint
    val detectObject:ObjectInterface

    init {
        detectObject = context as ObjectInterface
        boxPaint = Paint()
        boxPaint.color = Color.WHITE
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = STROKE_WIDTH


        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.textSize = TEXT_SIZE


    }

    override fun draw(canvas: Canvas) {
        // Draws the bounding box.
        detectObject.detectedObject(getCategoryName(visionObject.classificationCategory))
        val rect = RectF(visionObject.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, boxPaint)

        // Draws other object info.
        canvas.drawText(
            getCategoryName(visionObject.classificationCategory),
            rect.left,
            rect.bottom,
            textPaint
        )
        visionObject.trackingId?.let {
            canvas.drawText("id: $it", rect.left, rect.top, textPaint)
        }
        visionObject.classificationConfidence?.let {
            canvas.drawText("confidence: $it", rect.right, rect.bottom, textPaint)
        }
    }

    companion object {
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f

        private fun getCategoryName(@FirebaseVisionObject.Category category: Int): String {

            var detectedobject:String? = null

            when (category) {
                FirebaseVisionObject.CATEGORY_UNKNOWN -> detectedobject = "Unknown"
                FirebaseVisionObject.CATEGORY_HOME_GOOD -> detectedobject = "Home good"
                FirebaseVisionObject.CATEGORY_FASHION_GOOD -> detectedobject = "Fashion good"
                FirebaseVisionObject.CATEGORY_PLACE -> detectedobject = "Place"
                FirebaseVisionObject.CATEGORY_PLANT -> detectedobject = "Plant"
                FirebaseVisionObject.CATEGORY_FOOD -> detectedobject = "Food"
            } // fall out

            return detectedobject!!
        }
    }

}