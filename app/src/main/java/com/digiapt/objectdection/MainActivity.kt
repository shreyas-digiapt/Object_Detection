package com.digiapt.objectdection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.digiapt.objectdection.Util.CameraSource
import com.digiapt.objectdection.Util.ObjectInterface
import com.digiapt.objectdection.firebase_data.viewModel.MainViewModel
import com.google.firebase.ml.vision.objects.FirebaseVisionObjectDetectorOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : AppCompatActivity(), ObjectInterface {

    private var cameraSource:CameraSource? = null
    private var viewModel:MainViewModel? = null
    private var detectedObj:String? =null

    private final val TAG:String = "Main123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)


        setCameraForObject()
    }

    private fun setCameraForObject() {
        Log.d("test_123", "asd: ${(cameraSource == null) }")
        if (cameraSource == null) {
            cameraSource = CameraSource(this, fireFaceOverlay)
        }
        val objectDetectorOptions = FirebaseVisionObjectDetectorOptions.Builder()
            .setDetectorMode(FirebaseVisionObjectDetectorOptions.STREAM_MODE)
            .enableClassification().build()

        cameraSource?.setMachineLearningFrameProcessor(ObjectDetectorProcessor(this, objectDetectorOptions))
    }

    override fun onResume() {
        super.onResume()

        startCameraSource()
//        getObjectDetails()
    }

    override fun onPause() {
        super.onPause()

        try {
            cameraSource?.let {
                firePreview.stop()
            }
        }catch (e:Exception) {
            Log.d(TAG, "pause: Preview is null ===== ${e.message}")
        }

        stopListner()
    }

    private fun startCameraSource() {
        cameraSource?.let {
            try {
                if (firePreview == null) {
                    Log.d(TAG, "resume: Preview is null")
                }
                if (fireFaceOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null")
                }
                firePreview?.start(cameraSource, fireFaceOverlay)
            } catch (e: IOException) {
                Log.e(TAG, "Unable to start camera source.", e)
                cameraSource?.release()
                cameraSource = null
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun stopListner() {
        try {
            viewModel?.removeListner()
        }catch (e:Exception) {
            Log.d("test_22", "sadasd:  ${e.message}")
        }
    }

    fun getObjectDetails(objectDetected:String) {
        viewModel!!.getObjectDetails(objectDetected).observe(this, Observer { model->

            Log.d("qwe123", "asd: ${model==null}")

            try {
                if (model != null){
                    Log.d("test_123444", "model: ${model.objectName}")
                    frame_object_details.visibility = View.VISIBLE
                    tv_text.setText("TITLE     : ${model.objectName}")
                    tv_des.setText("DESCRIPTION: ${model.objectDes}")
                }else {
                    frame_object_details.visibility = View.GONE
                }
            }catch (e:java.lang.Exception) {
                Log.d("test_12345", "Exception: ${e.message}")
            }
        })
    }

    override fun detectedObject(detectedObject: String) {
        if (!detectedObj.equals(detectedObject)) {
            detectedObj = detectedObject
            getObjectDetails(detectedObject)
        }

    }
}
