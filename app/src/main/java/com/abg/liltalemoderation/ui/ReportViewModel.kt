package com.abg.liltalemoderation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abg.liltalemoderation.data.remote.Resource
import com.abg.liltalemoderation.data.repository.ComplaintRepository
import com.abg.liltalemoderation.model.pojo.ComplaintReport
import ir.logicbase.livex.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

class ReportViewModel(private val complaintRepository: ComplaintRepository) : ViewModel() {

    var reports: MutableLiveData<Resource<List<ComplaintReport>>> = MutableLiveData()
    val postDelete: SingleLiveEvent<Resource<Int>> = SingleLiveEvent()
    val complaintDelete: SingleLiveEvent<Resource<Int>> = SingleLiveEvent()

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }

    fun getReports() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        reports.postValue(complaintRepository.getReports())
    }

    fun removePost(idPost: Long) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        postDelete.postValue(complaintRepository.removePost(idPost))
    }

    fun complaintRemove(complaintReport: ComplaintReport) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        complaintDelete.postValue(complaintRepository.removeComplaint(complaintReport))
    }
}

@Suppress("UNCHECKED_CAST")
class ReportViewModelFactory(private val complaintRepository: ComplaintRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReportViewModel(complaintRepository) as T
    }
}