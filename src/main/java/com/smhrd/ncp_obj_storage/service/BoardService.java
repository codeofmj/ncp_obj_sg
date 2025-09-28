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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class BoardService {

    //생성자가 하나만 있을 경우 @AutoWired를 생략해도 스프링 컨테이너가 알아서 객체를 만들어서 주입
    private final S3Client s3Client;

    public BoardService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Value("${ncp.bucket-name}")
    private String bucketName;

    @Value("${ncp.end-point}")
    private String endPoint;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file){

        String imgPath = "";

        try{
            //1. 파일 이름 생성 및 준비
            String img_name = file.getOriginalFilename();
            String file_name = UUID.randomUUID() + "_" + img_name;
            Path path = Paths.get(uploadDir+file_name);

            //2.서버 PC에 저장
            file.transferTo(path);

            //3.NCP Object Storage에 저장
            // - PutObjectRequest: Object Storage에 파일 업로드하기 위한 요청 객체 생성
            //   -> 버킷이름, 파일이름, 파일형식(MIME), 파일접근권한
            // - s3Client.putObject()
            //   -> 준비된 요청객체와 파일데이터를 Object Storage로 파일 전송
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(file_name)
                    .contentType(file.getContentType())
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            imgPath = endPoint + "/" + bucketName + "/" + file_name;

            return imgPath;

        }catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    public byte[] downloadFile(String fileName){
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asByteArray();
    }
}
