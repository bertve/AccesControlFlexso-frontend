package com.flexso.flexsame.utils

object Utilities {
    fun capitalizeWords(s: String): String {
        var res: String = ""
        s.split(" ").map {
            res += it.capitalize() + " "
        }
        return res.trim()
    }

    private val HEX_CHARS = "0123456789ABCDEF"
    fun hexStringToByteArray(data: String): ByteArray {

        val result = ByteArray(data.length / 2)

        for (i in 0 until data.length step 2) {
            val firstIndex = HEX_CHARS.indexOf(data[i])
            val secondIndex = HEX_CHARS.indexOf(data[i + 1])

            val octet = firstIndex.shl(4).or(secondIndex)
            result.set(i.shr(1), octet.toByte())
        }

        return result
    }

    private val HEX_CHARS_ARRAY = "0123456789ABCDEF".toCharArray()
    fun toHex(byteArray: ByteArray): String {
        val result = StringBuffer()

        byteArray.forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS_ARRAY[firstIndex])
            result.append(HEX_CHARS_ARRAY[secondIndex])
        }

        return result.toString()
    }
}