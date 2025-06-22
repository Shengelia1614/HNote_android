package com.example.hnote


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hnote.databinding.ItemExampleBinding



class Adapter (fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments = listOf(
        Player.newInstance("Page 1"),
        PlayListHost(),
        //PageFragment.newInstance("Page 3")
    )

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]
}