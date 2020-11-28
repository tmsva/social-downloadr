package free.thirdpack.instadownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import free.thirdpack.instadownloader.adapters.DownloadAdapter
import free.thirdpack.instadownloader.databinding.FragmentHistoryBinding
import free.thirdpack.instadownloader.viewmodels.HistoryViewModel

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val adapter = DownloadAdapter()
        with(binding.downloadHistory) {
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setAdapter(adapter)
        }
        subscribeUi(adapter)
        return binding.root
    }

    private fun subscribeUi(adapter: DownloadAdapter) {
        viewModel.downloads.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

}