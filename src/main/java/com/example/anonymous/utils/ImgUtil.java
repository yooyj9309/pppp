package com.example.anonymous.utils;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.example.anonymous.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


public class ImgUtil {
    private static final String basicFilePath = "default_image.png";
    private static final String UPLOAD_FOLDER = "C:\\Users\\yooyj9309\\IdeaProjects\\anonymousboard\\src\\main\\resources\\static\\Images";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);

    public static String imgUpload(MultipartFile inputFile, String savedFilePath) {
        String currentFilePath = basicFilePath;
        String fileName = inputFile.getOriginalFilename();

        UUID uuid = UUID.randomUUID();

        if(!StringUtils.isEmpty(fileName)) {
            currentFilePath = uuid.toString()+"_"+fileName;
            File file = new File(UPLOAD_FOLDER,currentFilePath);
            try {
                inputFile.transferTo(file);
            } catch (IOException e) {
                throw new ServerException("서버 에러입니다.");
            }
        }else{
            if(!StringUtils.isEmpty(savedFilePath)){
                currentFilePath = savedFilePath;
            }
        }
        LOGGER.info(currentFilePath);
        return "images/"+currentFilePath;
    }


}