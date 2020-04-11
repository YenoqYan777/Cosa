package com.example.cosa.repository

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CacheStore private constructor(private val externalCacheDir: String) {
    private var cacheMap: HashMap<String, String>
    private val bitmapMap: HashMap<String, Bitmap>


    private fun cleanCacheStart() {
        cacheMap = HashMap()
        val fullCacheDir = File(
            externalCacheDir,
            cacheDir
        )
        fullCacheDir.mkdirs()
        val noMedia = File(fullCacheDir.toString(), ".nomedia")
        try {
            noMedia.createNewFile()
            Log.i("CACHE", "Cache created")
        } catch (e: IOException) {
            Log.i("CACHE", "Couldn't create .nomedia file")
            e.printStackTrace()
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun saveCacheFile(cacheUri: String, image: Bitmap) {
        val fullCacheDir = File(
            externalCacheDir,
            cacheDir
        )
        val fileLocalName =
            SimpleDateFormat("ddMMyyhhmmssSSS").format(Date()) + ".PNG"
        val fileUri = File(fullCacheDir.toString(), fileLocalName)
        var outStream: FileOutputStream? = null
        try {
            outStream = FileOutputStream(fileUri)
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()
            cacheMap[cacheUri] = fileLocalName
            Log.i(
                "CACHE",
                "Saved file $cacheUri (which is now $fileUri) correctly"
            )
            bitmapMap[cacheUri] = image
            val os = ObjectOutputStream(
                BufferedOutputStream(
                    FileOutputStream(
                        File(
                            fullCacheDir.toString(),
                            CACHE_FILENAME
                        )
                    )
                )
            )
            os.writeObject(cacheMap)
            os.close()
        } catch (e: FileNotFoundException) {
            Log.i("CACHE", "Error: File $cacheUri was not found!")
            e.printStackTrace()
        } catch (e: IOException) {
            Log.i("CACHE", "Error: File could not be stuffed!")
            e.printStackTrace()
        }
    }

    fun getCacheFile(cacheUri: String): Bitmap? {
        if (bitmapMap.containsKey(cacheUri)) return bitmapMap[cacheUri]
        if (!cacheMap.containsKey(cacheUri)) return null
        val fileLocalName = cacheMap[cacheUri].toString()
        val fullCacheDir = File(
            externalCacheDir,
            cacheDir
        )
        val fileUri = File(fullCacheDir.toString(), fileLocalName)
        if (!fileUri.exists()) return null
        Log.i("CACHE", "File $cacheUri has been found in the Cache")
        val bm = BitmapFactory.decodeFile(fileUri.toString())
        bitmapMap[cacheUri] = bm
        return bm
    }


    companion object {
        private var INSTANCE: CacheStore? = null
        private const val cacheDir = "/Android/data/com.cosa/cache/"
        private const val CACHE_FILENAME = ".cache"

        @Synchronized
        private fun createInstance(externalCacheDir: String) {
            if (INSTANCE == null) {
                INSTANCE = CacheStore(externalCacheDir)
            }
        }

        fun instance(externalCacheDir: String): CacheStore? {
            if (INSTANCE == null) createInstance(externalCacheDir)
            return INSTANCE
        }
    }

    init {
        cacheMap = HashMap()
        bitmapMap = HashMap()
        val fullCacheDir = File(
            externalCacheDir,
            cacheDir
        )
        if (!fullCacheDir.exists()) {
            Log.i("CACHE", "Directory doesn't exist")
            cleanCacheStart()

        }
        try {
            val inputStream = ObjectInputStream(
                BufferedInputStream(
                    FileInputStream(
                        File(
                            fullCacheDir.toString(),
                            CACHE_FILENAME
                        )
                    )
                )
            )
            cacheMap = inputStream.readObject() as HashMap<String, String>
            inputStream.close()
        } catch (e: StreamCorruptedException) {
            Log.i("CACHE", "Corrupted stream")
            cleanCacheStart()
        } catch (e: FileNotFoundException) {
            Log.i("CACHE", "File not found")
            cleanCacheStart()
        } catch (e: IOException) {
            Log.i("CACHE", "Input/Output error")
            cleanCacheStart()
        } catch (e: ClassNotFoundException) {
            Log.i("CACHE", "Class not found")
            cleanCacheStart()
        }
    }
}