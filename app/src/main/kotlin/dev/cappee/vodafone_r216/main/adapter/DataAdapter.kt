package dev.cappee.vodafone_r216.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.cappee.vodafone_r216.R
import dev.cappee.vodafone_r216.api.Data
import dev.cappee.vodafone_r216.api.Data.*
import dev.cappee.vodafone_r216.databinding.LayoutHeaderBinding
import dev.cappee.vodafone_r216.databinding.LayoutRecyclerBinding

class DataAdapter(
    private val context: Context,
    private val data: MutableList<Pair<Data, String>>,
    private val title: Int? = null
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class Header(val binding: LayoutHeaderBinding) : RecyclerView.ViewHolder(binding.root)

    inner class ViewHolder(val binding: LayoutRecyclerBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            Header(LayoutHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        } else {
            ViewHolder(LayoutRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            with(holder.binding) {
                textviewDescription.text = context.getString(data[position - 1].first.string)
                textviewValue.text = data[position - 1].second
                imageButtonTool.apply {
                    when (data[position - 1].first) {
                        VOLUME_DATA -> {
                            setImageDrawable(context.getDrawable(R.drawable.ic_data_usage))
                            setOnClickListener {

                            }
                        }
                        DEVICE_CONNECTED -> {
                            setImageDrawable(context.getDrawable(R.drawable.ic_devices))
                            setOnClickListener {

                            }
                        }
                        else -> {
                            visibility = View.GONE
                        }
                    }
                }
            }
        } else if (holder is Header) {
            if (title != null) {
                holder.binding.headerTitle.text = context.getString(title)
            } else {
                holder.binding.headerTitle.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return if (title != null) {
            data.size + 1
        } else {
            data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && title != null) {
            0
        } else {
            1
        }
    }
}