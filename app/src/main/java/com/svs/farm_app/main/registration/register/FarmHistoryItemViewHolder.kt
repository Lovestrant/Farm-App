package com.svs.farm_app.main.registration.register

import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.svs.farm_app.R

class FarmHistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mainLin: LinearLayout? = null
    private var ivDelete: AppCompatImageView? = null
    private var tvCropName: AppCompatTextView? = null
    private var tvCropDetail: AppCompatTextView? = null
    var srl: SwipeRevealLayout? = null

    fun bind(
        farmHistoryItem: FarmHistoryItem,
        selectedCropInteractionListener: FarmHistoryItemAdapter.SelectedCropInteractionListener,
        viewBinderHelper: ViewBinderHelper
    ) {
        mainLin = itemView.findViewById(R.id.mainLin)
        ivDelete = itemView.findViewById(R.id.ivDelete)
        tvCropName = itemView.findViewById(R.id.tvCropName)
        tvCropDetail = itemView.findViewById(R.id.tvCropDetail)
        srl = itemView.findViewById(R.id.srl)

        tvCropName?.text = farmHistoryItem.crop?.cropName
        tvCropDetail?.text = "${farmHistoryItem.acres} acre(s), ${farmHistoryItem.weight} kg(s)"

        viewBinderHelper.bind(srl, farmHistoryItem.crop?.cropID)
        mainLin?.setOnClickListener { selectedCropInteractionListener.onClicked(farmHistoryItem) }
        ivDelete?.setOnClickListener { selectedCropInteractionListener.onDeleted(farmHistoryItem) }
    }
}