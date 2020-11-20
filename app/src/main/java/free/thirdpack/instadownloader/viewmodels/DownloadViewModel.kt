package free.thirdpack.instadownloader.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import free.thirdpack.instadownloader.data.DownloadRepository
import kotlinx.coroutines.launch

class DownloadViewModel @ViewModelInject constructor(
    private val repository: DownloadRepository
) : ViewModel() {

    val mediaQueue = repository.mediaQueue
    val clipboardPaste = repository.clipboardPaste
    val downloads = repository.downloads

    fun clipboardPaste() = viewModelScope.launch {
        repository.clipboardPaste()
    }

    fun checkUrl(
        postUrl: String
    ) = viewModelScope.launch {
        repository.checkUrl(postUrl)
    }

    fun batchMediaDownload(
        selectedNodes: BooleanArray
    ) = viewModelScope.launch {
        repository.batchMediaDownload(selectedNodes)
    }
}