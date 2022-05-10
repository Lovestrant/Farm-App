package com.svs.farm_app.main.registration.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.svs.farm_app.R


class FarmAreaItemAdapter(var selectedFarmAreaItemInteractionListener: SelectedFarmAreaItemInteractionListener) :
    RecyclerView.Adapter<FarmAreaItemViewHolder>() {

    lateinit var farmAreaItemList: MutableList<FarmAreaItem>

    private val viewBinderHelper = ViewBinderHelper()

    override fun onBindViewHolder(@NonNull holder: FarmAreaItemViewHolder, position: Int) {
        holder.bind(
            farmAreaItemList[position],
            selectedFarmAreaItemInteractionListener,
            viewBinderHelper
        )
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): FarmAreaItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_selected_farm_area_item, parent, false)
        return FarmAreaItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return farmAreaItemList.size
    }

    fun setSelectedFarmAreaItems(farmAreaItems: MutableList<FarmAreaItem>) {
        this.farmAreaItemList = farmAreaItems
        this.notifyDataSetChanged()
    }

    fun removeSelectedCrop(farmAreaItem: FarmAreaItem) {
        this.farmAreaItemList.remove(farmAreaItem)
        this.notifyDataSetChanged()
    }

    interface SelectedFarmAreaItemInteractionListener {
        fun onDeleted(farmAreaItem: FarmAreaItem)
        fun onClicked(farmAreaItem: FarmAreaItem)
    }
}