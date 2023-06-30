package com.alphaomega.alphaomegarestfulapi.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FirebaseCloudStorageConfiguration {

    @Value("${application.config.firebase.private_key}")
    private String firebasePrivateKey;

    @Value("${application.config.firebase.bucket}")
    private String firebaseBucket;

    @Value("${application.config.firebase.download}")
    private String downloadUrl;



}
