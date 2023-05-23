package ad;


public class SmoothAnimationTimer {
    public float target;

    public float speed = 0.3f;

    public SmoothAnimationTimer(float target) {
        this.target = target;
    }

    public SmoothAnimationTimer(float target, float speed) {
        this.target = target;
        this.speed = speed;
    }

    public float value = 0;

    public boolean update(boolean increment) {
        this.value = AnimationUtils.smoothAnimation(value,target,60,0.3f);
        return value == target;
    }
}
