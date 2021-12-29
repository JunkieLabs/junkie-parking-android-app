package `in`.junkielabs.parking.ui.components.walkthrough

import `in`.junkielabs.parking.R
import `in`.junkielabs.parking.databinding.WalkThroughPagerItemBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * Created by niraj on 24-04-2021.
 */
class WalkThroughPagerAdapter() : RecyclerView.Adapter<WalkThroughPagerAdapter.PagerViewHolder>() {
    var data = arrayListOf(
        WalkThroughModel(
            "Manage",
            arrayOf(
                "Parking Area"
            ),
            "Handle Vehicle Parking spaces from the app.",
            R.drawable.pic_walk_through_manage

        ),
        WalkThroughModel(
            "Generate",
            arrayOf(
                "QR Code"
            ),
            "Unique QR code track of vehicles in any parking area",
            R.drawable.pic_walk_through_generate
        ),
        WalkThroughModel(
            "Collect",
            arrayOf(
                "Payment"
            ),
            "Auto bill generation inside app on checkout.",
            R.drawable.pic_walk_through_collect
        )

    )

    var mListener: WalkThroughPagerAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {

/*        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.walk_through_pager_item, parent, false)*/
        return PagerViewHolder.from(parent)
    }

    override fun getItemCount(): Int {

        return data.size
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        var dataItem = data.get(position)
        holder.bind(dataItem)
//        holder.vBtn.setOnClickListener {  var posi = holder.adapterPosition
//            if(posi>=0){
//
//                mListener?.onWalkThroughPagerAdapterItemClicked(position)
//            }}
        //To change body of created functions use File | Settings | File Templates.
    }

    fun setWalkThroughPagerAdapterListener(pListener: WalkThroughPagerAdapterListener?) {
        this.mListener = pListener
    }


    class PagerViewHolder(val binding: WalkThroughPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): PagerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WalkThroughPagerItemBinding.inflate(layoutInflater, parent, false)
                return PagerViewHolder(binding)
            }
        }

//        var vImageView: ImageView = itemView.find(R.id.walk_through_pager_view_image)
//        var vTitle: TextView= itemView.find(R.id.walk_through_pager_view_title)
//        var vTextView1: TextView = itemView.find(R.id.walk_through_pager_view_detail_text1)

        /*//        var vTextView2: TextView = itemView.find(R.id.walk_through_pager_view_detail_text2)
                //        var vBtn: FrameLayout = itemView.find(R.id.app_home_pager_item_btn)*/
        fun bind(model: WalkThroughModel) {

            if (model.messages.isNotEmpty()) {

                binding.walkThroughPagerItemDetailText1.text = model.messages[0]
//                if(model.messages.size>1){
//                    vTextView2.text = model.messages[1]
//                    vTextView2.visibility = View.VISIBLE
//                }else{
//                    vTextView2.visibility = View.GONE
//                }

            }

            binding.walkThroughPagerItemTitle.text = model.title

            binding.walkThroughPagerItemDetails.text = model.details
//
//            vImageView.setImageDrawable(
//                ContextCompat.getDrawable(
//                itemView.context,
//                model.asset
//            ))
            Glide.with(binding.walkThroughPagerItemImage)
                .load(model.asset)
                .thumbnail(0.5f)
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //                    .transform(new CircleTransform(mContext))
                //                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.walkThroughPagerItemImage)
        }

    }

    interface WalkThroughPagerAdapterListener {
        fun onWalkThroughPagerAdapterItemClicked(position: Int)
    }
}