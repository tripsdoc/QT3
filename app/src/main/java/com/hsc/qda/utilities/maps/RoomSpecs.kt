package com.hsc.qda.utilities.maps

class RoomSpecs(
    var imageWidth: Int = 0,
    var imageHeight: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
    var west: Double = 0.0,
    var north: Double = 0.0,
    var east: Double = 0.0,
    var south: Double = 0.0
) {

    fun getRoomSpec(room: String): RoomSpecs {
        return when(room) {
            "107" -> RoomSpecs(1080, 482, 1300, 580, -4.879518, 53.012048, 125.240963, -5.120481)
            "108" -> RoomSpecs(1013, 434, 1266, 542, -2.857142, 77.142858, 123.116883, 133.857142)
            "109" -> RoomSpecs(1010, 435, 1254, 540, -2.857142, 27.142858, 123.116883, 85.857142)
            "110" -> RoomSpecs(1029, 400, 1147, 456, 8.857142,-2.068965, 112.448979, 37.068965)
            "121" -> RoomSpecs(874, 670, 508, 390, -1.159420, -1.451631, 37.451631, 49.507246)
            "122" -> RoomSpecs(880, 667, 511, 388, -1.159420, 34.548369, 73.451631, 49.507246)
            "111" -> RoomSpecs(1013, 434, 1266, 542, -2.857142, -2.068965, 123.116883, 50.068965)
            "112" -> RoomSpecs(1010, 435, 1254, 540, -2.857142, 27.142858, 123.116883, 82.857142)
            "fullmap" -> RoomSpecs(860, 783, 1287, 1262, -4.032258, 123.548387, 133.870967, -3.387096)
            "full_12x" -> RoomSpecs(767, 527, 767, 527, -2.4, 50.4, 74.4, -2.4)
            else -> RoomSpecs()
        }
    }
}