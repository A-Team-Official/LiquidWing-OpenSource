package net.ccbluex.liquidbounce.utils.misc

import net.ccbluex.liquidbounce.utils.render.RenderUtils
import java.util.*
import kotlin.math.pow

object RandomUtils {
    fun nextBoolean(): Boolean {
        return Random().nextBoolean()
    }
    @JvmStatic
    fun nextInt(startInclusive: Int, endExclusive: Int): Int {
        return if (endExclusive - startInclusive <= 0) startInclusive else startInclusive + Random().nextInt(endExclusive - startInclusive)
    }

    fun nextDouble(startInclusive: Double, endInclusive: Double): Double {
        return if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0) startInclusive else startInclusive + (endInclusive - startInclusive) * Math.random()
    }

    fun nextFloat(startInclusive: Float, endInclusive: Float): Float {
        return if (startInclusive == endInclusive || endInclusive - startInclusive <= 0f) startInclusive else (startInclusive + (endInclusive - startInclusive) * Math.random()).toFloat()
    }

    fun randomNumber(length: Int): String {
        return random(length, "123456789")
    }

    @JvmStatic
    fun randomString(length: Int): String {
        return random(length, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz")
    }

    fun random(length: Int, chars: String): String {
        return random(length, chars.toCharArray())
    }

    fun random(length: Int, chars: CharArray): String {
        val stringBuilder = StringBuilder()
        for (i in 0 until length) stringBuilder.append(chars[Random().nextInt(chars.size)])
        return stringBuilder.toString()
    }
    var tiph = 0f
    var tiphrun = 0f
    fun animtips(easing: Int) {
        tiph += ((easing - tiph) / 2.0F.pow(10.0F - 2.5f)) * RenderUtils.deltaTime
    }
    fun animtipsrun(easing: Int) {
        tiphrun += ((easing - tiphrun) / 2.0F.pow(10.0F - 2.5f)) * RenderUtils.deltaTime
    }
}