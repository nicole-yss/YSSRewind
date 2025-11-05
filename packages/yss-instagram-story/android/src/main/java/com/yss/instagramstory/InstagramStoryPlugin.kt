
package com.yss.instagramstory

import android.content.Intent
import android.net.Uri
import android.util.Base64
import androidx.core.content.FileProvider
import com.getcapacitor.*
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@NativePlugin
class InstagramStoryPlugin : Plugin() {

  @PluginMethod
  fun shareVideo(call: PluginCall) {
    val srcType = call.getString("sourceType")
    val source  = call.getString("source")
    val appId   = call.getString("appId")
    val contentUrl = call.getString("contentUrl") ?: ""
    val topColor    = call.getString("topColor") ?: "#000000"
    val bottomColor = call.getString("bottomColor") ?: "#000000"
    val mimeType    = call.getString("mimeType") ?: "video/*"

    if (srcType==null || source==null || appId==null) { call.reject("Missing params"); return }

    val ctx = bridge.activity
    CoroutineScope(Dispatchers.IO).launch {
      try {
        val file = when (srcType) {
          "remoteUrl" -> downloadToCache(source)
          "filePath"  -> File(if (source.startsWith("file://")) Uri.parse(source).path!! else source)
          "base64"    -> writeBase64(source)
          else        -> null
        } ?: throw Exception("No media file")

        val uri = FileProvider.getUriForFile(
          ctx,
          ctx.packageName + ".instagramstory.fileprovider",
          file
        )

        val intent = Intent("com.instagram.share.ADD_TO_STORY").apply {
          setDataAndType(uri, mimeType)
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
          putExtra("source_application", appId)          // Meta App ID
          putExtra("content_url", contentUrl)
          putExtra("top_background_color", topColor)
          putExtra("bottom_background_color", bottomColor)
          setPackage("com.instagram.android")
        }

        ctx.grantUriPermission("com.instagram.android", uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)

        withContext(Dispatchers.Main) {
          if (intent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(intent)
            call.resolve()
          } else {
            call.reject("Instagram not installed")
          }
        }
      } catch (e: Exception) {
        withContext(Dispatchers.Main) { call.reject(e.message) }
      }
    }
  }

  private fun downloadToCache(urlStr: String): File {
    val out = File(bridge.activity.cacheDir, "ig_${'$'}{System.currentTimeMillis()}.mp4")
    URL(urlStr).openStream().use { it.copyTo(FileOutputStream(out)) }
    return out
  }

  private fun writeBase64(b64: String): File {
    val out = File(bridge.activity.cacheDir, "ig_${'$'}{System.currentTimeMillis()}.mp4")
    FileOutputStream(out).use { it.write(Base64.decode(b64, Base64.DEFAULT)) }
    return out
  }
}
