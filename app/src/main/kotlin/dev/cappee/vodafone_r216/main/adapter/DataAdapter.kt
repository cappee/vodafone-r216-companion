package dev.cappee.vodafone_r216.main.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import dev.cappee.vodafone_r216.R
import dev.cappee.vodafone_r216.api.Data
import dev.cappee.vodafone_r216.api.Data.*
import dev.cappee.vodafone_r216.databinding.LayoutHeaderBinding
import dev.cappee.vodafone_r216.databinding.LayoutRecyclerBinding
import java.util.*
import kotlin.math.roundToInt

class DataAdapter(
    private val _context: Context,
    private val data: MutableList<Pair<Data, String?>>,
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
                textviewDescription.text = _context.getString(data[position - 1].first.string)
                textviewValue.text = data[position - 1].second
                imageButtonTool.apply {
                    when (data[position - 1].first) {
                        VOLUME_DATA -> {
                            setImageDrawable(_context.getDrawable(R.drawable.ic_data_usage))
                            setOnClickListener {
                                val dialog = MaterialDialog(_context)
                                dialog.show {
                                    title(R.string.data_saver)
                                    customView(R.layout.dialog_data_saver)
                                    val editTextDataPlan = getCustomView().findViewById<EditText>(R.id.editTextMontlyPlan)
                                    val editTextDate = getCustomView().findViewById<EditText>(R.id.editTextDate)
                                    val editTextStart = getCustomView().findViewById<EditText>(R.id.editTextStart)
                                    editTextDate.addTextChangedListener(object : TextWatcher {
                                        private var current = ""
                                        private val ddmmyyyy = "DDMMYYYY"
                                        private val calendar = Calendar.getInstance()

                                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                            if (!s.toString().equals(current)) {
                                                var clean: String = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
                                                val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")
                                                val cl = clean.length
                                                var sel = cl
                                                var i = 2
                                                while (i <= cl && i < 6) {
                                                    sel++
                                                    i += 2
                                                }
                                                //Fix for pressing delete next to a forward slash
                                                if (clean == cleanC) sel--
                                                if (clean.length < 8) {
                                                    clean = clean + ddmmyyyy.substring(clean.length)
                                                } else {
                                                    //This part makes sure that when we finish entering numbers
                                                    //the date is correct, fixing it otherwise
                                                    var day = clean.substring(0, 2).toInt()
                                                    var mon = clean.substring(2, 4).toInt()
                                                    var year = clean.substring(4, 8).toInt()
                                                    mon =
                                                        if (mon < 1) 1 else if (mon > 12) 12 else mon
                                                    calendar.set(Calendar.MONTH, mon - 1)
                                                    year =
                                                        if (year < 1900) 1900 else if (year > 2100) 2100 else year
                                                    calendar.set(Calendar.YEAR, year)
                                                    // ^ first set year for the line below to work correctly
                                                    //with leap years - otherwise, date e.g. 29/02/2012
                                                    //would be automatically corrected to 28/02/2012
                                                    day =
                                                        if (day > calendar.getActualMaximum(Calendar.DATE)) calendar.getActualMaximum(
                                                            Calendar.DATE
                                                        ) else day
                                                    clean = String.format(
                                                        "%02d%02d%02d",
                                                        day,
                                                        mon,
                                                        year
                                                    )
                                                }
                                                clean = String.format(
                                                    "%s/%s/%s", clean.substring(0, 2),
                                                    clean.substring(2, 4),
                                                    clean.substring(4, 8)
                                                )
                                                sel = if (sel < 0) 0 else sel
                                                current = clean
                                                editTextDate.setText(current)
                                                editTextDate.setSelection(if (sel < current.length) sel else current.length)
                                            }
                                        }

                                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                                        override fun afterTextChanged(s: Editable?) {}
                                    })
                                    positiveButton(R.string.ok, click = object : DialogCallback {
                                        override fun invoke(p1: MaterialDialog) {
                                            val modemUsedData = textviewValue.text.dropLast(2).toString().toDouble()
                                            val monthlyPlan = editTextDataPlan.text.toString().toDouble()
                                            val monthlyStartedAt = editTextStart.text.toString().toDouble()
                                            textviewValue.text = String.format("%.1f", monthlyPlan - (modemUsedData - monthlyStartedAt)) + "GB"
                                        }
                                    })
                                }
                            }
                        }
                        DEVICE_CONNECTED -> {
                            setImageDrawable(_context.getDrawable(R.drawable.ic_devices))
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
                holder.binding.headerTitle.text = _context.getString(title)
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