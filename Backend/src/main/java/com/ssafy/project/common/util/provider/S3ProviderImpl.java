package com.ssafy.project.common.util.provider;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Log4j2
@Service
public class S3ProviderImpl implements S3Provider {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    @Transactional
    @Override
    public String uploadMultipartFile(MultipartFile file, String uri) throws IOException {

        if (file != null || !file.isEmpty()) {

            s3Client.putObject(new PutObjectRequest(bucket, uri, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return s3Client.getUrl(bucket, uri).toString();
        }

        else {
            throw new FileNotFoundException("파일이 존재하지 않거나 비어있습니다.");
        }
    }

    @Transactional
    @Override
    public String uploadFile(File file, String uri) throws IOException {

        if (file != null && file.isFile()) {

            s3Client.putObject(new PutObjectRequest(bucket, uri, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            return s3Client.getUrl(bucket, uri).toString();
        }

        else {
            throw new FileNotFoundException("파일이 존재하지 않거나 비어있습니다.");
        }
    }

    @Override
    public void delete(String uri, int start) {
        log.info("삭제 비교 UUID : {}", uri.substring(start, 54));
        s3Client.deleteObject(bucket, uri.substring(start, 54)); }
}
