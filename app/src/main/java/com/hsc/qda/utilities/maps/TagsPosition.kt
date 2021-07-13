package com.hsc.qda.utilities.maps

class TagsPosition (
    var x: Double = 0.0,
    var y: Double = 0.0
) {
    fun getPosition(x: Double, y: Double, room: String) : TagsPosition {
        return when(room) {
            "107" -> TagsPosition(x,y)
            "108" -> TagsPosition(y,x)
            "109" -> TagsPosition(y,x)
            "110" -> TagsPosition(y,x)
            "111" -> TagsPosition(y,x)
            "112" -> TagsPosition(y,x)
            "121" -> TagsPosition(y,x)
            "122" -> TagsPosition(y,x)
            "fullmap" -> TagsPosition(x,y)
            "full_12x" -> TagsPosition(x,y)
            else -> TagsPosition()
        }
    }

    fun getPosition(x: Any, y: Any, room: String) : TagsPosition {
        this.x = getTagDouble(x)
        this.y = getTagDouble(y)

        return getPosition(this.x, this.y, room)
    }

    private fun getTagDouble(tagData : Any) : Double {
        return when(tagData) {
            is Int -> tagData.toDouble()
            is Double -> tagData
            else -> tagData as Double
        }
    }
}

