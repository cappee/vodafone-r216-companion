package dev.cappee.vodafone_r216.api

import dev.cappee.vodafone_r216.R

enum class Data(val string: Int) {
    VOLUME_DATA(R.string.volume_data),
    BATTERY(R.string.battery),
    PHONE_NUMBER(R.string.phone_number),
    SIGNAL_STRENGTH(R.string.signal_strength),
    SIGNAL_STATUS(R.string.signal_status),
    NETWORK(R.string.network),
    DEVICE_CONNECTED(R.string.device_connected)
}