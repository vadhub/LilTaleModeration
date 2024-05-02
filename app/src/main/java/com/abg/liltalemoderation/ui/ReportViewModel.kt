package com.abg.liltalemoderation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.abg.liltalemoderation.data.remote.RemoteInstance
import com.abg.liltalemoderation.data.remote.Resource
import com.abg.liltalemoderation.data.repository.ComplaintRepository
import com.abg.liltalemoderation.model.pojo.ComplaintReport
import com.abg.liltalemoderation.model.pojo.User
import ir.logicbase.livex.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReportViewModel(private val complaintRepository: ComplaintRepository) : ViewModel() {

    var error: SingleLiveEvent<Exception> = SingleLiveEvent()
    var success: SingleLiveEvent<Unit> = SingleLiveEvent()
    var reports: MutableLiveData<List<ComplaintReport>> = MutableLiveData()
    val postDelete: SingleLiveEvent<Resource<Int>> = SingleLiveEvent()
    val complaintDelete: SingleLiveEvent<Resource<Int>> = SingleLiveEvent()

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        throwable.printStackTrace()
    }

    fun auth(username: String, password: String) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        RemoteInstance.setUser(User(username, password))
        when(val resource= complaintRepository.getReports()) {
            is Resource.Loading -> {}

            is Resource.Success -> {
                success.postValue(Unit)
            }

            is Resource.Failure -> {
                error.postValue(resource.exception)
            }
        }
    }

    fun fetchReports() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        reports.postValue((complaintRepository.getReports() as Resource.Success).result)
    }

    fun removePost(idPost: Long, idComplaint: Long) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        postDelete.postValue(complaintRepository.removePost(idPost))
        complaintRemove(idComplaint)
    }

    fun complaintRemove(id: Long) = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        complaintDelete.postValue(complaintRepository.removeComplaint(id))
        reports.postValue(reports.value?.filter { it.id != id })
    }
}

@Suppress("UNCHECKED_CAST")
class ReportViewModelFactory(private val complaintRepository: ComplaintRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReportViewModel(complaintRepository) as T
    }
}