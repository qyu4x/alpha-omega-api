package com.alphaomega.alphaomegarestfulapi.util;

import com.alphaomega.alphaomegarestfulapi.exception.BadRequestException;
import com.alphaomega.alphaomegarestfulapi.exception.FailedUploadFileException;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileCheckerUtil {

    public static Boolean uploadVideoChecker(String title, MultipartFile videoSource) {
        if (title == null) {
            throw new BadRequestException("Title can't be empty");
        }
        if (videoSource.isEmpty()) {
            throw new FailedUploadFileException("Oops video can't be empty, please select video first");
        }

        return true;
    }
}
