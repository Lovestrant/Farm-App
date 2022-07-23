package com.svs.farm_app.main.registration.register

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.svs.farm_app.R
import com.svs.farm_app.main.registration.register.steps.FarmHistoryFragment


class FarmHistoryItemAdapter(var selectedCropInteractionListener: SelectedCropInteractionListener) :
    RecyclerView.Adapter<FarmHistoryItemViewHolder>() {

    lateinit var farmHistoryItemList: MutableList<FarmHistoryItem>


    private val viewBinderHelper = ViewBinderHelper()

    override fun onBindViewHolder(@NonNull holder: FarmHistoryItemViewHolder, position: Int) {
        holder.bind(farmHistoryItemList[position], selectedCropInteractionListener, viewBinderHelper)
    }

    @NonNull
    override fun onCreateViewHolder(
        @NonNull parent: ViewGroup,
        viewType: Int
    ): FarmHistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_selected_crop_item, parent, false)
        return FarmHistoryItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return farmHistoryItemList.size
    }

    fun setSelectedCrops(farmHistoryItems: MutableList<FarmHistoryItem>) {
        this.farmHistoryItemList = farmHistoryItems
        this.notifyDataSetChanged()
    }

    fun removeSelectedCrop(farmHistoryItem: FarmHistoryItem) {
        this.farmHistoryItemList.remove(farmHistoryItem)
        this.notifyDataSetChanged()
    }

    interface SelectedCropInteractionListener {
        fun onDeleted(farmHistoryItem: FarmHistoryItem)
        fun onClicked(farmHistoryItem: FarmHistoryItem)


    }
}