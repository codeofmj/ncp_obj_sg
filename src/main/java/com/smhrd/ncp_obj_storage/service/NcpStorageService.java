package com.smhrd.ncp_obj_storage.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.smhrd.ncp_obj_storage.dto.FileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NcpStorageService {
    private final AmazonS3 amazonS3;

    @Value("${ncp.s3.bucketName}") private String bucketName;
    @Value("${ncp.s3.endpoint}") private String endPoint;

    private String buildObjectUrl(String endPoint, String bucket, String key){
        String e = endPoint.endsWith("/") ? endPoint.substring(0, endPoint.length()-1) : endPoint;
        return e + "/" + bucket + key;
    }

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    //NOTICE: filePath의 맨 앞에 /는 안붙여도됨. ex) history/images
    public List<FileDTO> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {

        List<FileDTO> s3files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {

            if(multipartFile.isEmpty()) continue;

                String originalFileName = multipartFile.getOriginalFilename();
                String uploadFileName = getUuidFileName(originalFileName != null ? originalFileName  : "file");
                String keyName = filePath + "/" + uploadFileName;

                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(multipartFile.getSize());
                objectMetadata.setContentType(multipartFile.getContentType());

                try (InputStream in = multipartFile.getInputStream()) {

                    PutObjectRequest req = new PutObjectRequest(bucketName, keyName, in, objectMetadata);

                    // S3에 폴더 및 파일 업로드
                    amazonS3.putObject(req);
                    String uploadFileUrl = buildObjectUrl(endPoint, bucketName, keyName);

                    s3files.add(
                            FileDTO.builder()
                                    .originalFileName(originalFileName)
                                    .uploadFileName(uploadFileName)
                                    .uploadFilePath(filePath)
                                    .uploadFileUrl(uploadFileUrl)
                                    .build());


                } catch (IOException e) {
                    e.printStackTrace();
                }

        }// end for

        return s3files;
    }

}