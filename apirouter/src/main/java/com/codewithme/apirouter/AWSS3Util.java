package com.codewithme.apirouter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectTagging;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.SetObjectTaggingRequest;
import com.amazonaws.services.s3.model.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AWSS3Util {

    private AmazonS3 s3;

    public AWSS3Util(AWSCredentialsProvider credentials) {
        s3 = AmazonS3ClientBuilder.standard().withCredentials(credentials).withRegion(Regions.US_EAST_1).build();
    }

    public boolean doesFileExist(String bucket, String s3Path) {
        return (s3.doesObjectExist(bucket, s3Path));
    }

    public Properties getPropertiesFile(String bucket, String s3Path) throws AmazonClientException, IOException {
        Properties properties = new Properties();
        try (S3ObjectInputStream propsStream = s3.getObject(new GetObjectRequest(bucket, s3Path)).getObjectContent()) {
            properties.load(propsStream);
        }
        
        return properties;
    }
    
    public AmazonS3 getS3Client() {
        return s3;
    }
    
    public String getS3ObjectAsString(String bucket, String key) throws AmazonClientException, IOException {
        return s3.getObjectAsString(bucket, key);
    }
    
    public void putS3Object(String bucket, String key, String data) throws AmazonClientException, IOException {
        s3.putObject(bucket, key, data);
    }
    
    public void putS3Object(String bucket, String key, String data, Map<String, String> metadata) throws AmazonClientException, IOException {
        s3.putObject(bucket, key, data);
        List<Tag> tagSet = new ArrayList<>(metadata.size());
        for (String metakey : metadata.keySet()) {
            tagSet.add(new Tag(metakey, metadata.get(metakey)));
        }
        
        ObjectTagging tagging = new ObjectTagging(tagSet);
        SetObjectTaggingRequest taggingRequest = new SetObjectTaggingRequest(bucket, key, tagging);
        s3.setObjectTagging(taggingRequest);
    }
    
}
