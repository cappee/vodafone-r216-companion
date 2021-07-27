package dev.cappee.vodafone_r216.api

import dev.cappee.vodafone_r216.api.Data.*
import java.util.*

object Parser {

    fun getData(html: String): MutableList<Pair<Data, Any>> {
        val data : MutableList<Pair<Data, Any>> = mutableListOf()
        val scanner = Scanner(html)
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            if (line.contains("mobileDataVolumeText")) {
                data.add(Pair(VOLUME_DATA, line.substring(119, 127)))
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
                data.add(Pair(NETWORK, line.substring(113, 120)))
            }
            if (line.contains("TR_BATTERY_STATUS")) {
                println("BATTERY:$line")
                //data.add(Pair(BATTERY, line.substring(108, 111)))
            }
            if (line.contains("connectedDevicesList")) {
                println("DEVICE_CONNECTED:$line")
                //TODO: Split connected device name in the right way
            }
        }
        return data
    }

}