package com.smhrd.ncp_obj_storage.controller;

import com.smhrd.ncp_obj_storage.service.NcpStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/board")
public class NcpStorageController {

    @Autowired
    private NcpStorageService service;

    @GetMapping("/")
    public String getUpload(){
        return "upload";
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadFilesSample(
            @RequestParam(value = "files") List<MultipartFile> multipartFiles) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.uploadFiles(multipartFiles, "sample-folder"));
    }


}