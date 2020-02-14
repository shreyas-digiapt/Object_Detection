package com.digiapt.objectdection

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.digiapt.objectdection.Util.CameraImageGraphic
import com.digiapt.objectdection.Util.FrameMetaData
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.objects.FirebaseVisionObject
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetector
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import java.io.IOException

class ObjectDetectorProcessor (private val context: Context, options: FirebaseVisionObjectDetectorOptions) : VisionProcessorBase<List<FirebaseVisionObject>>() {


    private final val TAG:String = "test123"

    private val detector: FirebaseVisionObjectDetector


    init {
        detector = FirebaseVision.getInstance().getOnDeviceObjectDetector(options)
    }

    override fun stop() {
        super.stop()

        try {
            detector.close()
        }catch (e:IOException) {
            Log.e(TAG, "Exception thrown while trying to close object detector: $e")
        }
    }

    override fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionObject>> {
        return detector.processImage(image)
    }

    override fun onSuccess(
        originalCameraImage: Bitmap?,
        results: List<FirebaseVisionObject>,
        frameMetadata: FrameMetaData,
        graphicOverlay: GraphicsOverlay
    ) {
        graphicOverlay.clear()
        if (originalCameraImage != null) {
            val imageGraphic = CameraImageGraphic(graphicOverlay, originalCameraImage)
            graphicOverlay.add(imageGraphic)
        }
        for (visionObject in results) {
            Log.d("ooi8", "asd: ${visionObject.classificationCategory}")
            val objectGraphic = ObjectGraphics(context,graphicOverlay, visionObject)
            graphicOverlay.add(objectGraphic)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
    }

}