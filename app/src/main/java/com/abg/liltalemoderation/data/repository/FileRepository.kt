package com.abg.liltalemoderation.data.repository

import android.widget.ImageView
import com.abg.liltalemoderation.data.local.AudioDao
import com.abg.liltalemoderation.data.local.SaveInternalHandle
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.model.pojo.Audio

class FileRepository(private val saveHandle: SaveInternalHandle, private val audioDao: AudioDao, private val remoteInstance: RemoteInstance) {

    suspend fun getUriByAudio(audio: Audio): String {

        if (audioDao.getById(audio.id) == null) {
            val response = remoteInstance.apiFileHandle().downloadAudio(audio.id)
            if (response.isSuccessful) {
                response.body()?.byteStream()?.let {
                    val file = saveHandle.saveFile(audio.uri, it)
                    audioDao.insert(audio.copy(uri = file))
                    return file
                }
            }
        }

        return audioDao.getById(audio.id)?.uri ?: ""
    }

    fun getIcon(userId: Long, imageView: ImageView, invalidate: Boolean) {
        remoteInstance.apiIcon(imageView, userId, invalidate)
    }

    fun getImage(imageId: Long?, imageView: ImageView) {
        remoteInstance.apiImage(imageView, imageId)
    }
}