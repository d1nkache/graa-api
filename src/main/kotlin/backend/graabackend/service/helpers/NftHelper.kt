package backend.graabackend.service.helpers

import org.apache.commons.codec.binary.Hex

fun hexStringToByteArray(hex: String): ByteArray {
    return Hex.decodeHex(hex.toCharArray())
}

fun extractBits(byteArray: ByteArray, bitOffset: Int, bitLength: Int): Int {
    require(bitLength <= 32) { "Битовая длина должна быть не больше 32" }

    var value = 0
    var bitIndex = bitOffset

    for (i in 0 until bitLength) {
        val byteIndex = bitIndex / 8
        val bitIndexInByte = bitIndex % 8
        if (byteIndex >= byteArray.size) break

        val bit = (byteArray[byteIndex].toInt() shr bitIndexInByte) and 1
        value = (value shl 1) or bit
        bitIndex++
    }

    return value
}