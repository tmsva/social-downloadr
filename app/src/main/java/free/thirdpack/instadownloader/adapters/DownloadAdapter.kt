package free.thirdpack.instadownloader.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import free.thirdpack.instadownloader.data.DownloadMedia
import free.thirdpack.instadownloader.databinding.ListItemDownloadBinding

class DownloadAdapter : ListAdapter<DownloadMedia, DownloadAdapter.ViewHolder>(
        DownloadDiffCallback()
) {

    class ViewHolder(
            private val binding: ListItemDownloadBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(download: DownloadMedia) {
            with(binding) {
                setDownload(download)
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(
                    ListItemDownloadBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    )
            )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class DownloadDiffCallback : DiffUtil.ItemCallback<DownloadMedia>() {

    override fun areItemsTheSame(oldItem: DownloadMedia, newItem: DownloadMedia): Boolean =
            oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: DownloadMedia, newItem: DownloadMedia): Boolean {
        return oldItem == newItem
    }
}
