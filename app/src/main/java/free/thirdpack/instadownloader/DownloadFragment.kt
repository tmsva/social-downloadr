package free.thirdpack.instadownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import free.thirdpack.instadownloader.databinding.FragmentDownloadBinding
import free.thirdpack.instadownloader.viewmodels.DownloadViewModel

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private val viewModel: DownloadViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDownloadBinding.inflate(inflater, container, false)
        binding.model = viewModel
        return binding.root
    }

}