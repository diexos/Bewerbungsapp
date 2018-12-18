package com.example.sbaar.bewerbungsapp


import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.data_check.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DataCheckFragment : Fragment() {
    val TAG = "DataCheckFragment"
    var pic_update = 0
    override fun onAttach(context: Context?) {
        Log.d(TAG, "onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        setHasOptionsMenu(true)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView")

        val v = inflater.inflate(R.layout.data_check, container, false)
        val iV_click: ImageView = v.findViewById(R.id.imageView19)
        val iV_click2: ImageView = v.findViewById(R.id.imageView20)
        val iV_click3: ImageView = v.findViewById(R.id.imageView21)
        val iV_click4: ImageView = v.findViewById(R.id.imageView22)
        iV_click.setOnClickListener{dispatchTakePictureIntent(iV_click)
                                    pic_update = 1 }
        iV_click2.setOnClickListener{dispatchTakePictureIntent(iV_click2)
            pic_update = 2 }
        iV_click3.setOnClickListener{dispatchTakePictureIntent(iV_click3)
            pic_update = 3 }
        iV_click4.setOnClickListener{dispatchTakePictureIntent(iV_click4)
            pic_update = 4 }


        return v
    }
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.menu_search).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


    override fun onResume() {
        when(pic_update)
        {
            1 -> setPic(imageView19)
            2 -> setPic(imageView20)
            3 -> setPic(imageView21)
            4 -> setPic(imageView22)
            else -> pic_update = 0

        }
        super.onResume()
    }


  lateinit var mCurrentPhotoPath: String

    @Throws(IOException::class)
    fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.GERMANY).format(Date())
        val storageDir: File? = (activity as MainMenuActivity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath

        }
    }

    val REQUEST_TAKE_PHOTO = 1

    private fun dispatchTakePictureIntent(v:ImageView) {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity((activity as MainMenuActivity).packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            (activity as MainMenuActivity),
                            "com.example.android.fileprovider",
                            it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }

    }



    private fun setPic(v:ImageView) {
        // Get the dimensions of the View
        val targetW: Int = v.width
        val targetH: Int = v.height

        val bmOptions =BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(mCurrentPhotoPath, this)
            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor

        }
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)?.also { bitmap ->
            v.setImageBitmap(bitmap)

        }
    }


}


