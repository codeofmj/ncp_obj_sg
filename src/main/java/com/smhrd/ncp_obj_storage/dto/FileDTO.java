package com.smhrd.ncp_obj_storage.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FileDTO {

    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;

}
