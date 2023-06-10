package com.alphaomega.alphaomegarestfulapi.service;

import com.alphaomega.alphaomegarestfulapi.payload.response.CloudUploadVideoResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;

public interface FirebaseCloudStorageService {

    String doUploadImageFile(MultipartFile multipartFile);
    CloudUploadVideoResponse doUploadVideoFile(MultipartFile multipartFile);
    String uploadFile(File file, String fileName);
    File convertToFile(MultipartFile multipartFile, String fileName) throws FileNotFoundException;
    String getVideoExtension(String fileName);
    String getImageExtension(String fileName);

}
