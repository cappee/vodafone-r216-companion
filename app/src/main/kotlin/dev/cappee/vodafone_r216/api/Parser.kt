package dev.cappee.vodafone_r216.api

import java.util.*
import dev.cappee.vodafone_r216.api.Data.VOLUME_DATA

object Parser {

    fun getData(html: String): MutableList<Pair<Data, Any>> {
        val data : MutableList<Pair<Data, Any>> = mutableListOf()
        val scanner = Scanner(html)
        while (scanner.hasNext()) {
            val line = scanner.nextLine()
            if (line.contains("mobileDataVolumeText")) {
                data.add(Pair(VOLUME_DATA, line.substring(119, 127)))
            }
        }
        return data
    }

}