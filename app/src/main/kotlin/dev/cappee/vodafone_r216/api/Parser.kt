package dev.cappee.vodafone_r216.api

import dev.cappee.vodafone_r216.api.Data.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.*

object Parser {

    fun getData(html: String): MutableList<Pair<Data, String?>> {
        with(Jsoup.parse(html)) {
            return mutableListOf(
                Pair(PHONE_NUMBER,
                    getElementById("mobileNumberText")!!.text()),
                Pair(SIGNAL_STRENGTH,
                    getElementById("mobileSignaltext")!!.className()),
                Pair(SIGNAL_STATUS,
                    getElementById("mobileStatusText")!!.text()),
                Pair(VOLUME_DATA,
                    getElementById("mobileDataVolumeText")!!.text()),
                Pair(NETWORK,
                    getElementById("mobileNetworkText")!!.text()),
                Pair(BATTERY,
                    getElementsByClass("TR_BATTERY_STATUS").text()
                        .replace(".*\\(|\\).*".toRegex(), "")),
                Pair(DEVICE_CONNECTED,
                    getElementsByClass("TR_CONNECTED_DEVICES").text()
                        .replace(".*\\(|\\).*".toRegex(), ""))
            )
        }
    }

}