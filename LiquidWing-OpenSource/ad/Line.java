//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ad;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Line {

    public SmoothAnimationTimer posXTimer = new SmoothAnimationTimer(1.0f, 0.4f);
    public SmoothAnimationTimer posYTimer = new SmoothAnimationTimer(1.0f, 0.4f);
    public SmoothAnimationTimer alphaTimer = new SmoothAnimationTimer(1.0f, 0.15f);
    public float tempY = 0;
    public float y = 0;
    private final int chatLineID;
    public boolean a;

    private final int updateCounterCreated;
    private final ITextComponent lineString;

    public Line(int p_i45000_1_, ITextComponent p_i45000_2_, int p_i45000_3_) {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
    }

    public ITextComponent getChatComponent() {
        return this.lineString;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }

    public int getChatLineID() {
        return this.chatLineID;
    }
}
