package sparkle.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

public final class ByteBufferUtils {
    public static ByteBuffer resize(ByteBuffer buffer, int newCapacity) {
        var newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer fromInputStream(InputStream inputStream) {
        try {
            var rbc = Channels.newChannel(inputStream);
            var buffer = BufferUtils.createByteBuffer(2);
            while (true) {
                var bytes = rbc.read(buffer);
                if (bytes == -1) {
                    break;
                }
                if (buffer.remaining() == 0) {
                    buffer = resize(buffer, buffer.capacity() * 3 / 2);
                }
            }
            rbc.close();
            buffer.flip();
            return MemoryUtil.memSlice(buffer);
        } catch (IOException e) {
            throw new RuntimeException("Could not read buffer from input stream" + e.getMessage(), e);
        }
    }
}