package free.thirdpack.instadownloader.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import free.thirdpack.instadownloader.DownloadFragment
import free.thirdpack.instadownloader.HistoryFragment

const val DOWNLOAD_PAGE_INDEX = 0
const val HISTORY_PAGE_INDEX = 1

class HomeViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments: Map<Int, () -> Fragment> = mapOf(
        DOWNLOAD_PAGE_INDEX to { DownloadFragment() },
        HISTORY_PAGE_INDEX to { HistoryFragment() }
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments[position]?.invoke() ?: throw IndexOutOfBoundsException()
}
