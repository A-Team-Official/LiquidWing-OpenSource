
package ad.utils

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.utils.render.tenacity.FileUtils
import java.io.File

class TipSoundManager {
    var enableSound : TipSoundPlayer
    var disableSound : TipSoundPlayer

    init {
        val enableSoundFile=File(LiquidBounce.fileManager.soundsDir,"enable.wav")
        val disableSoundFile=File(LiquidBounce.fileManager.soundsDir,"disable.wav")

        if(!enableSoundFile.exists())
            FileUtils.unpackFile(enableSoundFile,"assets/minecraft/liquidwing/sounds/enable.wav")
        if(!disableSoundFile.exists())
            FileUtils.unpackFile(disableSoundFile,"assets/minecraft/liquidwing/sounds/disable.wav")

        enableSound=TipSoundPlayer(enableSoundFile)
        disableSound=TipSoundPlayer(disableSoundFile)
    }
}