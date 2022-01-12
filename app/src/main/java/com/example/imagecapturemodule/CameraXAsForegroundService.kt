package com.example.imagecapturemodule

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.util.Size
import android.view.Surface
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.CallSuper
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ServiceLifecycleDispatcher
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DateFormat
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraXAsForegroundService : LifecycleService(), LifecycleOwner {

    var executor: Executor? = null
    private val mDispatcher = ServiceLifecycleDispatcher(this)
    private var imageCapture: ImageCapture? = null
    var fileList: ArrayList<String>? = null
    override fun onCreate() {
        mDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
        createChannel()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    @CallSuper
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val input = intent!!.getStringExtra("inputExtra")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        if ("CAPTURE" == intent.action) {
            Handler().postDelayed({ takePhoto() }, 5000)
        }
        Log.d(TAG, "onStartCommand:")
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("foreground service")
            .setContentText(input)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        startCamera()
        return START_NOT_STICKY
    }

    @CallSuper
    override fun onDestroy() {
        mDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }

    @CallSuper
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        mDispatcher.onServicePreSuperOnBind()
        return null
    }

    @CallSuper
    override fun onStart(intent: Intent?, startId: Int) {
        mDispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun getLifecycle(): Lifecycle {
        return mDispatcher.lifecycle
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindPreview(cameraProvider)
                Handler().postDelayed({ takePhoto() }, 5000)
                //  takePhoto();
            } catch (e: ExecutionException) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            } catch (e: InterruptedException) {
            }
        }, ContextCompat.getMainExecutor(this))
    }

    fun takePhoto() {
        val photoFile = File(filesDir, "photoApp-" + System.currentTimeMillis() + ".jpg")
        val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        //  getBitmap(getFilesDir().getPath());
        val bmOptions = BitmapFactory.Options()
        val image = BitmapFactory.decodeFile(photoFile.absolutePath, bmOptions)

        Log.d("imageeee", "img$image")
        fileList = ArrayList()
        fileList!!.add("photoApp-" + System.currentTimeMillis() + ".jpg")
        imageCapture!!.takePicture(
            output,
            ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(applicationContext, "image saved", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.IO).launch {
                        val compressedImage = Compressor.compress(applicationContext, photoFile)
                        Log.d(TAG, "compressedImage" + compressedImage.name + "compressed")

                        //save file into internal dir
                        val filePath = compressedImage.path
                        val bitmap = BitmapFactory.decodeFile(filePath)

                        storeImage(bitmap)
                        val intent = Intent("intentKey")
                        intent.putExtra("file_path", filePath)
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                        Log.d(TAG, "t" + bitmap.byteCount)
                    }


                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            })
        Log.d("photo_name", "name" + photoFile.name)
        for (i in 0..photoFile.length()) {
            Log.d("photo_name", "array_item" + fileList!![i.toInt()])
        }

        //  sendMessageToActivity(DateFormat.getInstance().format(System.currentTimeMillis()));
    }

    fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
        executor = Executors.newSingleThreadExecutor()
        imageCapture = ImageCapture.Builder().build()
        val imageAnalysis = (executor as ExecutorService?)?.let {
            ImageAnalysis.Builder()
                .setTargetResolution(Size(100, 100))
                .setBackgroundExecutor(it)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_BLOCK_PRODUCER)
                .setImageQueueDepth(5)
                .setTargetRotation(Surface.ROTATION_90)
                .build()
        }
        (executor as ExecutorService?)?.let {
            imageAnalysis?.setAnalyzer(it, ImageAnalysis.Analyzer { image: ImageProxy ->
                val rotationDegrees = image.imageInfo.rotationDegrees
                val grayImage = ProxyImageToGrayScale(image)
                image.close()
            })
        }
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, imageCapture)
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun ProxyImageToGrayScale(imageOrg: ImageProxy): ByteArray {
        imageOrg.image.use { image ->
            val planes = image!!.planes
            val yBuffer = planes[0].buffer
            val uBuffer = planes[1].buffer
            val vBuffer = planes[2].buffer
            val ySize = yBuffer.remaining()
            val nv21 = ByteArray(ySize)
            //U and V are swapped
            yBuffer[nv21, 0, ySize]
            return nv21
        }
    }


    companion object {
        private const val CHANNEL_ID = "101"
        private const val TAG = "CAmeraX"
    }

    private fun storeImage(image: Bitmap) {
        try {
            val mImageName = "photoApp-" + System.currentTimeMillis() + "compressed" + ".jpg"
            val fos = openFileOutput(mImageName, MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.PNG, 50, fos)

            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}