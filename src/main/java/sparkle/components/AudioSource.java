package sparkle.components;

import sparkle.assets.Audio;
import sparkle.core.Component;

public final class AudioSource extends Component {
    private final Audio audio;
    private final boolean playOnStart;

    public AudioSource(Audio audio, boolean playOnStart) {
        this.audio = audio;
        this.playOnStart = playOnStart;
    }

    @Override
    protected void start() {
        if (playOnStart && audio.getState() == Audio.State.STOPPED) {
            audio.play();
        }
    }

    @Override
    protected void destroy() {
        if (audio.getState() == Audio.State.PLAYING) {
            audio.stop();
        }
    }

    public Audio getAudio() {
        return audio;
    }
}