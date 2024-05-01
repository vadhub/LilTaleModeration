package com.abg.liltalemoderation.ui

import android.media.Image
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abg.liltalemoderation.data.remote.Resource
import com.abg.liltalemoderation.data.repository.FileRepository
import com.abg.liltalemoderation.model.pojo.Audio
import com.google.android.material.imageview.ShapeableImageView
import ir.logicbase.livex.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FileViewModel(private val fileRepository: FileRepository) : ViewModel() {

    val uriAudio: SingleLiveEvent<String> = SingleLiveEvent()

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }

    fun getUri(audio: Audio) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        uriAudio.postValue(fileRepository.getUriByAudio(audio))
    }

    fun getImage(id: Long?, imageViewPost: ImageView) {
        if (id != null) fileRepository.getImage(id, imageViewPost)
    }

    fun getIcon(userId: Long, imageIcon: ShapeableImageView) {
        fileRepository.getIcon(userId, imageIcon, false)
    }

    fun invalidate(userId: Long, imageIcon: ShapeableImageView) {
        fileRepository.getIcon(userId, imageIcon, true)
    }
}

@Suppress("UNCHECKED_CAST")
class LoadViewModelFactory(private val repository: FileRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FileViewModel(repository) as T
    }
}