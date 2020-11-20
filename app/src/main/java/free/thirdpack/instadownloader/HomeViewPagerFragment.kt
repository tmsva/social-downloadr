package free.thirdpack.instadownloader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import free.thirdpack.instadownloader.adapters.DOWNLOAD_PAGE_INDEX
import free.thirdpack.instadownloader.adapters.HISTORY_PAGE_INDEX
import free.thirdpack.instadownloader.adapters.HomeViewPagerAdapter
import free.thirdpack.instadownloader.databinding.FragmentHomeViewPagerBinding

class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false)

        with(binding) {
            pager.adapter = HomeViewPagerAdapter(this@HomeViewPagerFragment)
            mediate(tabs, pager)
        }

        return binding.root
    }

    private fun mediate(tabs: TabLayout, pager: ViewPager2) {
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.setText(getTextResId(position))
        }.attach()
    }

    private fun getTextResId(position: Int): Int =
        when (position) {
            DOWNLOAD_PAGE_INDEX -> R.string.downloads
            HISTORY_PAGE_INDEX -> R.string.history
            else -> R.string.empty
        }
}