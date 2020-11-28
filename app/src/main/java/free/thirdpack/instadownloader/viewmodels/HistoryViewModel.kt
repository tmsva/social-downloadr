package free.thirdpack.instadownloader.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import free.thirdpack.instadownloader.data.DownloadRepository

class HistoryViewModel @ViewModelInject constructor(
        repository: DownloadRepository
) : ViewModel() {

    val downloads = repository.downloads
}