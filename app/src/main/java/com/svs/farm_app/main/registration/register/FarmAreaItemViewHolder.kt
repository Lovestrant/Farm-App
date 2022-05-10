package com.svs.farm_app.main.registration.register

import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.svs.farm_app.R

class FarmAreaItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var mainLin: LinearLayout? = null
    private var ivDelete: AppCompatImageView? = null
    private var tvFarmAreaVillage: AppCompatTextView? = null
    private var tvCropFarmAreaSize: AppCompatTextView? = null
    var srl: SwipeRevealLayout? = null

    fun bind(
        farmAreaItem: FarmAreaItem,
        selectedCropInteractionListener: FarmAreaItemAdapter.SelectedFarmAreaItemInteractionListener,
        viewBinderHelper: ViewBinderHelper
    ) {
        mainLin = itemView.findViewById(R.id.mainLin)
        ivDelete = itemView.findViewById(R.id.ivDelete)
        tvFarmAreaVillage = itemView.findViewById(R.id.tvFarmAreaVillage)
        tvCropFarmAreaSize = itemView.findViewById(R.id.tvFarmAreaSize)
        srl = itemView.findViewById(R.id.srl)

        tvFarmAreaVillage?.text = farmAreaItem.village?.villageName
        tvCropFarmAreaSize?.text = "${farmAreaItem.estimatedFarmArea} acre(s)"

        viewBinderHelper.bind(srl, farmAreaItem.village?.villageID)
        mainLin?.setOnClickListener { selectedCropInteractionListener.onClicked(farmAreaItem) }
        ivDelete?.setOnClickListener { selectedCropInteractionListener.onDeleted(farmAreaItem) }
    }
}