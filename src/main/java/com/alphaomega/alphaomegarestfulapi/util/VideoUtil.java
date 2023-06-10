package com.alphaomega.alphaomegarestfulapi.util;

import com.github.kokorin.jaffree.ffprobe.FFprobe;
import com.github.kokorin.jaffree.ffprobe.FFprobeResult;

import java.io.File;

public class VideoUtil {

    public static Integer getDurationVideo(String absolutePath) {
        FFprobeResult result = FFprobe.atPath()
                .setShowStreams(true)
                .setInput(absolutePath)
                .execute();

        return result.getStreams().stream()
                .filter(stream -> "video".equals(stream.getCodecType()))
                .findFirst()
                .map(stream -> stream.getDuration().intValue())
                .orElse(0);
    }

}
