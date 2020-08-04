package jp.techacademy.date.yuuya.autoslideshowapp2

import android.Manifest
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.database.Cursor
import android.os.Handler
import android.provider.MediaStore
import android.content.ContentUris
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var mCursor: Cursor? = null

    private val PERMISSIONS_REQUEST_CODE = 100

    private var mTimer: Timer? = null

    // タイマー用の時間のための変数
    private var mTimerSec = 0.0

    private var mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                button1.isClickable = false
                button2.isClickable = false
                button3.isClickable = false
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mCursor != null)
            mCursor!!.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        mCursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )
        mCursor!!.moveToFirst()
        val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
        val id = mCursor!!.getLong(fieldIndex)
        val imageUri =
            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

        imageView.setImageURI(imageUri)

        button1.setOnClickListener {


            if (mCursor!!.moveToNext()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = mCursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } else {
                mCursor!!.moveToFirst()
                val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = mCursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }
        }

        button2.setOnClickListener {
            if (mTimer == null){
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        button1.isClickable = false
                        button3.isClickable = false
                        mHandler.post {
                            button2.text = "停止"
                            if (mCursor!!.moveToNext()) {
                                // indexからIDを取得し、そのIDから画像のURIを取得する
                                val fieldIndex =
                                    mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = mCursor!!.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )

                                imageView.setImageURI(imageUri)
                            } else {
                                mCursor!!.moveToFirst()
                                val fieldIndex =
                                    mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                                val id = mCursor!!.getLong(fieldIndex)
                                val imageUri =
                                    ContentUris.withAppendedId(
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        id
                                    )

                                imageView.setImageURI(imageUri)
                            }
                        }
                    }
                }, 2000, 2000) // 最初に始動させるまで 2000ミリ秒、ループの間隔を 2000ミリ秒 に設定
            }
            else{
                mTimer!!.cancel()
                mTimer = null
                button1.isClickable = true
                button3.isClickable = true
                mHandler.post{
                    button2.text = "再生"
                }
            }
        }

        button3.setOnClickListener {


            if (mCursor!!.moveToPrevious()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = mCursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            } else {
                mCursor!!.moveToLast()
                val fieldIndex = mCursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                val id = mCursor!!.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }
        }
    }
}

