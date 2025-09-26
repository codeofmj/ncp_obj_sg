package com.smhrd.ncp_obj_storage.controller;

import com.smhrd.ncp_obj_storage.service.NcpStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/files")
public class NcpStorageController {

    private final NcpStorageService ncpStorageService;

    public NcpStorageController(NcpStorageService ncpStorageService) {
        this.ncpStorageService = ncpStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = ncpStorageService.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        // 네이버 클라우드에서 파일 다운로드 URL을 가져오는 서비스 호출
        byte[] data = ncpStorageService.downloadFile(fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        // 응답에 파일 데이터와 메타정보 설정
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(resource);
    }
}