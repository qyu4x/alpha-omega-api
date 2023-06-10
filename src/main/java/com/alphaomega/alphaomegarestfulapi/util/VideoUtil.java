package com.alphaomega.alphaomegarestfulapi.util;


import org.jcodec.common.DemuxerTrack;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.demuxer.MP4Demuxer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;


public class VideoUtil {

    private static final Logger log = LoggerFactory.getLogger(VideoUtil.class);

    public static Integer getDurationVideo(String filePath) throws IOException {
        FileChannelWrapper fileChannelWrapper = NIOUtils.readableFileChannel(filePath);
        MP4Demuxer demuxer = MP4Demuxer.createMP4Demuxer(fileChannelWrapper);
        DemuxerTrack videoTrack = demuxer.getVideoTrack();
        log.info("video_duration: {}", videoTrack.getMeta().getTotalDuration());
        int durationInSecond = Double.valueOf(videoTrack.getMeta().getTotalDuration()).intValue();

        demuxer.close();
        fileChannelWrapper.close();

        return durationInSecond;
    }

}
