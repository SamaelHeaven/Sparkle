package sparkle.core;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class FPSTarget {
    public static final FPSTarget FPS_30 = new FPSTarget("FPS_30", 30);
    public static final FPSTarget FPS_45 = new FPSTarget("FPS_45", 45);
    public static final FPSTarget FPS_60 = new FPSTarget("FPS_60", 60);
    public static final FPSTarget FPS_90 = new FPSTarget("FPS_90", 90);
    public static final FPSTarget FPS_120 = new FPSTarget("FPS_120", 120);
    public static final FPSTarget FPS_144 = new FPSTarget("FPS_144", 144);
    public static final FPSTarget FPS_165 = new FPSTarget("FPS_165", 165);
    public static final FPSTarget FPS_240 = new FPSTarget("FPS_240", 240);
    public static final FPSTarget DISPLAY = new FPSTarget("DISPLAY", GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate());
    public static final FPSTarget NO_LIMIT = new FPSTarget("NO_LIMIT", Integer.MAX_VALUE);
    private static final List<FPSTarget> fpsTargets = new ArrayList<>();

    static {
        fpsTargets.add(FPS_30);
        fpsTargets.add(FPS_45);
        fpsTargets.add(FPS_60);
        fpsTargets.add(FPS_90);
        fpsTargets.add(FPS_120);
        fpsTargets.add(FPS_144);
        fpsTargets.add(FPS_165);
        fpsTargets.add(FPS_240);
        fpsTargets.add(DISPLAY);
        fpsTargets.add(NO_LIMIT);
    }

    public int target;
    private final String name;

    public FPSTarget(int target) {
        this(null, target);
    }

    private FPSTarget(String name, int target) {
        this.target = (target <= 0 ? 60 : target);
        this.name = Objects.requireNonNullElse(name, super.toString());
    }

    public static FPSTarget valueOf(String name) {
        for (var fpsTarget : fpsTargets) {
            if (fpsTarget.name.equals(name)) {
                return fpsTarget;
            }
        }
        return null;
    }

    public static FPSTarget[] values() {
        return fpsTargets.toArray(new FPSTarget[0]);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof FPSTarget fpsTarget) {
            return target == fpsTarget.target;
        }
        return false;
    }
}