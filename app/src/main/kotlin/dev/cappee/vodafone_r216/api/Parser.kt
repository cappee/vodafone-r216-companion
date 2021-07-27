package dev.cappee.vodafone_r216.api

import dev.cappee.vodafone_r216.api.Data.*
import java.util.*

object Parser {

    fun getData(html: String): MutableList<Pair<Data, String>> {
        val data : MutableList<Pair<Data, String>> = mutableListOf()
        val scanner = Scanner(html)
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            if (line.contains("mobileDataVolumeText")) {
                var value = line.substring(119, 127)
                if (value.takeLast(3) == "</d") {
                    value = value.dropLast(3)
                }
                if (value.takeLast(1) == "<") {
                    value = value.dropLast(1)
                }
                data.add(Pair(VOLUME_DATA, value))
            }
            if (line.contains("mobileNumberText")) {
                data.add(Pair(PHONE_NUMBER, line.substring(114, 124)))
            }
            if (line.contains("mobileSignaltext")) {
                data.add(Pair(SIGNAL_STRENGTH, line.substring(77, 95)))
            }
            if (line.contains("mobileSignalText")) {
                data.add(Pair(SIGNAL_STATUS, line.substring(97, 116)))
            }
            if (line.contains("mobileNetworkText")) {
                data.add(Pair(NETWORK, line.substring(113, 119)))
            }
            if (line.contains("TR_BATTERY_FULL_MESSAGE")) {
                data.add(Pair(BATTERY, line.substring(95, 98) + "%"))
            }
            if (line.contains("TR_CONNECTED_DEVICES")) {
                data.add(Pair(DEVICE_CONNECTED, line.substring(117, 118)))
                //TODO: Split connected device name in the right way
            }
        }
        return data
    }

}