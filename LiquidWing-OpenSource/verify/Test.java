package verify;

import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

import javax.swing.*;

public class Test extends MinecraftInstance implements Listenable {
    @Override
    public boolean handleEvents() {
        return true;
    }
    public Test() {
        new Thread(() -> {
            while (true) {
                try {
                    if (MySQLDemo.x2onlineinfo()){
                        JOptionPane.showMessageDialog(null,"管理员要求你强制下线","Error", JOptionPane.WARNING_MESSAGE);
                        mc.shutdown();
                        throw new Exception("The administrator requires you to be forcibly offline");
                    }
                    Thread.sleep(5000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}