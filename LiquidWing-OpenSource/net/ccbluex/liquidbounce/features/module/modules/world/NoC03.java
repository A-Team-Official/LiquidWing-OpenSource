package net.ccbluex.liquidbounce.features.module.modules.world;//package net.ccbluex.liquidbounce.features.module.modules.world;
//
//import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
//import net.ccbluex.liquidbounce.event.EventTarget;
//import net.ccbluex.liquidbounce.event.MoveEvent;
//import net.ccbluex.liquidbounce.event.PacketEvent;
//import net.ccbluex.liquidbounce.event.UpdateEvent;
//import net.ccbluex.liquidbounce.features.module.Module;
//import net.ccbluex.liquidbounce.features.module.ModuleCategory;
//import net.ccbluex.liquidbounce.features.module.ModuleInfo;
//import net.ccbluex.liquidbounce.utils.ClientUtils;
//import net.ccbluex.liquidbounce.value.BoolValue;
//
//@ModuleInfo(name = "NoC03", description = "No C03", category = ModuleCategory.HYT)
//public class NoC03 extends Module {
//
//    @EventTarget
//    public void onPacket(PacketEvent event){
//        IPacket packet = event.getPacket();
//        if(classProvider.isCPacketPlayer(packet)){
//            event.cancelEvent();
//        }
//    }
//    @EventTarget
//    public void onMove(MoveEvent event){
//        event.zero();
//    }
//}
