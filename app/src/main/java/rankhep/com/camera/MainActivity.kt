package rankhep.com.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQ_CODE_SELECT_IMAGE = 100
    private val TAKE_CAMERA = 2
    private var photoUri: Uri? = null
    private var currentPhotoPath = ""//실제 사진 파일 경로
    var mImageCaptureName = ""//이미지 이름


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        select.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
            intent.data = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)
        }

        takePicture.setOnClickListener {
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(packageManager) != null) {
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                    } catch (ex: IOException) {

                    }
                    if (photoFile != null) {
                        photoUri = FileProvider.getUriForFile(this, packageName, photoFile)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(intent, TAKE_CAMERA)
                    }
                }

            }
        }
    }

    fun createImageFile() :File {
        var dir = File("${Environment.getExternalStorageDirectory()}/path/")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        var timeStamp:String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        mImageCaptureName = timeStamp + ".png";

        var storageDir = File("${Environment.getExternalStorageDirectory().absoluteFile}/path/"
                + mImageCaptureName)
        currentPhotoPath = storageDir.absolutePath

        return storageDir

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode === REQ_CODE_SELECT_IMAGE) {
                try {
                    if (data != null)
                        img.setImageURI(data.data)
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } else if (requestCode == TAKE_CAMERA) {
                Log.e("asd", currentPhotoPath)
                img.setImageURI(Uri.fromFile(File(currentPhotoPath)))
            }
        }
    }


}
