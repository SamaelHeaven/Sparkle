package sparkle.core;

public final class Time {
    private static final int SLEEP_PRECISION = 2_000_000;
    private static Time instance = null;
    private final long startTime;
    private long lastFrameTime;
    private long frameCount;
    private float delta;
    private float averageFPS;

    private Time() {
        startTime = System.nanoTime();
    }

    public static long getTicks() {
        return System.nanoTime() - getInstance().startTime;
    }

    public static float getDelta() {
        return getInstance().delta;
    }

    public static float getFixedDelta() {
        return 1 / 60f;
    }

    public static float getAverageFPS() {
        return getInstance().averageFPS;
    }

    public static float getCurrentFPS() {
        return getDelta() == 0 ? 0 : 1 / getDelta();
    }

    static Time getInstance() {
        Game.throwIfUninitialized();
        return instance = instance == null ? new Time() : instance;
    }

    public void update() {
        sync();
        refresh();
    }

    private void refresh() {
        frameCount++;
        delta = (float) ((getTicks() - lastFrameTime) / 1e9);
        var timeInSeconds = (float) (getTicks() / 1e9);
        averageFPS = timeInSeconds == 0 ? 0 : (frameCount / timeInSeconds);
        lastFrameTime = getTicks();
    }

    private void sync() {
        var targetFrameTime = getTargetFrameTime();
        var waitTime = (long) (targetFrameTime - (getTicks() - lastFrameTime));
        if (waitTime > 0 && waitTime <= targetFrameTime) {
            try {
                sleep(waitTime);
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not sleep current thread: " + e.getMessage(), e);
            }
        }
    }

    private double getTargetFrameTime() {
        return 1_000_000_000.0 / Game.getFPSTarget().target;
    }

    private void sleep(long nanoSeconds) throws InterruptedException {
        var endTime = System.nanoTime() + nanoSeconds;
        var timeLeft = nanoSeconds;
        while (timeLeft > 0) {
            if (timeLeft > SLEEP_PRECISION) {
                Thread.sleep(1);
            } else {
                Thread.sleep(0);
            }
            timeLeft = endTime - System.nanoTime();
        }
    }
}