package com.alphaomega.alphaomegarestfulapi.service.implementation;

import com.alphaomega.alphaomegarestfulapi.configuration.FirebaseCloudStorageConfiguration;
import com.alphaomega.alphaomegarestfulapi.exception.FailedUploadFileException;
import com.alphaomega.alphaomegarestfulapi.exception.FileUploadException;
import com.alphaomega.alphaomegarestfulapi.service.FirebaseCloudStorageService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FirebaseCloudStorageServiceImpl implements FirebaseCloudStorageService {


    private final static Logger log = LoggerFactory.getLogger(FirebaseCloudStorageService.class);

    private FirebaseCloudStorageConfiguration firebaseCloudStorageConfiguration;

    public FirebaseCloudStorageServiceImpl(FirebaseCloudStorageConfiguration firebaseCloudStorageConfiguration) {
        this.firebaseCloudStorageConfiguration = firebaseCloudStorageConfiguration;
    }

    @Override
    public String doUploadImageFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String finalFileName = UUID.randomUUID().toString()
                .concat(getImageExtension(originalFilename));

        File file = convertToFile(multipartFile, finalFileName);
        String imageUrl = uploadFile(file, finalFileName);
        file.delete();

        return imageUrl;
    }

    @Override
    public String doUploadVideoFile(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String finalFileName = UUID.randomUUID().toString()
                .concat(getVideoExtension(originalFilename));

        File file = convertToFile(multipartFile, finalFileName);
        String videoUrl = uploadFile(file, finalFileName);
        file.delete();

        return videoUrl;
    }

    @Override
    public String uploadFile(File file, String fileName) {
        BlobId blobId = BlobId.of(firebaseCloudStorageConfiguration.getFirebaseBucket(), fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType("media").build();
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseCloudStorageConfiguration.getFirebasePrivateKey()).getInputStream());
            Storage storage = StorageOptions
                    .newBuilder().setCredentials(googleCredentials).build().getService();
            storage.create(blobInfo, Files.readAllBytes(file.toPath()));
            log.info("Successfully uploaded files with name {} ", fileName);
        } catch (IOException exception) {
            throw new FileUploadException(exception.getMessage());
        }
        return String.format(firebaseCloudStorageConfiguration.getDownloadUrl(), URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) {
        File file = new File(fileName);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(multipartFile.getBytes());
        } catch (IOException exception) {
            throw new FileUploadException(exception.getMessage());
        }
        return file;
    }

    @Override
    public String getVideoExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        log.info("extension {} ", extension);
        if (extension.equals(".mp4") || extension.equals(".mkv")) {
            return extension;
        } else {
            log.error("Video extension not available");
            throw new FailedUploadFileException("File must be video with extension mp4 or mkv");
        }
    }

    @Override
    public String getImageExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf("."));
        log.info("extension {} ", extension);
        if (extension.equals(".jpg") || extension.equals(".png") || extension.equals(".jpeg") || extension.equals(".jfif")) {
            return extension;
        } else {
            log.error("Image extension not available");
            throw new FailedUploadFileException("File must be image with extension jpg, png, or jfif");
        }
    }
}
