package sparkle.assets;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.libc.LibCStdlib;
import sparkle.utils.ByteBufferUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Audio {
    private static final ExecutorService executor = Executors.newCachedThreadPool(runnable -> {
        var thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });
    private static final List<Clip> globalClips = new ArrayList<>();
    private static long audioContext = MemoryUtil.NULL;
    private static long audioDevice = MemoryUtil.NULL;
    private static float globalVolume = 1;

    static {
        initializeOpenAL();
    }

    private final ByteBuffer inputStreamBuffer;
    private final boolean loop;
    private final List<Clip> clips;
    private final List<Clip> disposedClips;
    private float volume;

    public Audio(URL url) {
        this(url, 1);
    }

    public Audio(URL url, float volume) {
        this(url, volume, false);
    }

    public Audio(URL url, float volume, boolean loop) {
        inputStreamBuffer = loadInputStreamBuffer(Objects.requireNonNull(url));
        this.volume = clampVolume(volume);
        this.loop = loop;
        clips = new ArrayList<>();
        disposedClips = new ArrayList<>();
    }

    public static float getGlobalVolume() {
        return globalVolume;
    }

    public static void setGlobalVolume(float globalVolume) {
        var newVolume = clampVolume(globalVolume);
        if (Audio.globalVolume == newVolume) {
            return;
        }
        Audio.globalVolume = newVolume;
        for (var object : globalClips.toArray()) {
            var clip = (Clip) object;
            clip.setVolume(clip.volume);
        }
    }

    private static void initializeOpenAL() {
        openDevice();
        createContext();
        createCapabilities();
        Runtime.getRuntime().addShutdownHook(new Thread(Audio::disposeOpenAL));
    }

    private static void openDevice() {
        audioDevice = ALC11.alcOpenDevice(getDefaultDeviceName());
    }

    private static String getDefaultDeviceName() {
        return ALC11.alcGetString(0, ALC11.ALC_DEFAULT_DEVICE_SPECIFIER);
    }

    private static void createContext() {
        audioContext = ALC11.alcCreateContext(audioDevice, new int[]{0});
        ALC11.alcMakeContextCurrent(audioContext);
    }

    private static void createCapabilities() {
        var alcCapabilities = ALC.createCapabilities(audioDevice);
        var alCapabilities = AL.createCapabilities(alcCapabilities);
        if (!alCapabilities.OpenAL11) {
            throw new RuntimeException("Could not create OpenAL capabilities");
        }
    }

    private static void disposeOpenAL() {
        ALC11.alcDestroyContext(audioContext);
        ALC11.alcCloseDevice(audioDevice);
    }

    private static float clampVolume(float volume) {
        return Math.max(0, Math.min(1, volume));
    }

    public void play() {
        cleanClips();
        var clip = new Clip(inputStreamBuffer, volume, loop);
        globalClips.add(clip);
        clips.add(clip);
        clip.play();
        addDisposeClipListener(clip);
    }

    public void stop() {
        cleanClips();
        for (var clip : clips) {
            if (clip.getState() == State.PLAYING) {
                clip.stop();
            }
        }
        clips.clear();
    }

    public State getState() {
        cleanClips();
        for (var clip : clips) {
            if (clip.getState() == State.PLAYING) {
                return State.PLAYING;
            }
        }
        return State.STOPPED;
    }

    public boolean isLooping() {
        return loop;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        cleanClips();
        this.volume = clampVolume(volume);
        for (var clip : clips) {
            clip.setVolume(this.volume);
        }
    }

    private ByteBuffer loadInputStreamBuffer(URL url) {
        try (var inputStream = url.openStream()) {
            return ByteBufferUtils.fromInputStream(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Could not load audio: " + url.getPath(), e);
        }
    }

    private void cleanClips() {
        var temp = new ArrayList<>(disposedClips);
        clips.removeAll(temp);
        disposedClips.removeAll(temp);
    }

    private void addDisposeClipListener(Clip clip) {
        executor.submit(() -> {
            try {
                while (clip.getState() == State.PLAYING) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("Could not sleep current thread: " + e.getMessage(), e);
            } finally {
                clip.dispose();
                globalClips.remove(clip);
                disposedClips.add(clip);
            }
        });
    }

    public enum State {
        PLAYING, STOPPED
    }

    private static final class Clip {
        private final int sourceId;
        private final int bufferId;
        private float volume;

        private Clip(ByteBuffer inputStreamBuffer, float volume, boolean loop) {
            this.volume = volume;
            var channelsBuffer = BufferUtils.createIntBuffer(1);
            var sampleRateBuffer = BufferUtils.createIntBuffer(1);
            var rawAudioBuffer = STBVorbis.stb_vorbis_decode_memory(inputStreamBuffer, channelsBuffer, sampleRateBuffer);
            var format = getFormat(channelsBuffer.get(0));
            bufferId = AL11.alGenBuffers();
            AL11.alBufferData(bufferId, format, Objects.requireNonNull(rawAudioBuffer), sampleRateBuffer.get(0));
            sourceId = AL11.alGenSources();
            AL11.alSourcei(sourceId, AL11.AL_BUFFER, bufferId);
            AL11.alSourcei(sourceId, AL11.AL_LOOPING, (loop ? 1 : 0));
            AL11.alSourcei(sourceId, AL11.AL_POSITION, 0);
            AL11.alSourcef(sourceId, AL11.AL_GAIN, volume * globalVolume);
            LibCStdlib.free(rawAudioBuffer);
        }

        private int getFormat(int channels) {
            if (channels == 1) {
                return AL11.AL_FORMAT_MONO16;
            } else if (channels == 2) {
                return AL11.AL_FORMAT_STEREO16;
            }
            return -1;
        }

        private void play() {
            AL11.alSourcePlay(sourceId);
        }

        private void stop() {
            AL11.alSourceStop(sourceId);
        }

        private State getState() {
            return AL11.alGetSourcei(sourceId, AL11.AL_SOURCE_STATE) == AL11.AL_PLAYING ? State.PLAYING : State.STOPPED;
        }

        private void setVolume(float volume) {
            this.volume = volume;
            AL11.alSourcef(sourceId, AL11.AL_GAIN, volume * globalVolume);
        }

        private void dispose() {
            AL11.alDeleteSources(sourceId);
            AL11.alDeleteBuffers(bufferId);
        }
    }
}