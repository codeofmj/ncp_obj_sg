package com.smhrd.ncp_obj_storage.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class NcpStorageService {

    private final S3Client s3Client;

    @Value("${ncp.bucket-name}")
    private String bucketName;

    public NcpStorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();

            // S3 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            // 파일 업로드 실행
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            // 업로드된 파일의 URL 반환
            return "https://kr.object.ncloudstorage.com/" + bucketName + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    public byte[] downloadFile(String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)  // 버킷 이름
                .key(fileName)       // 다운로드할 파일 이름
                .build();

        // 파일을 다운로드하고 byte[]로 반환
        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
    }
}