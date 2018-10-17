package com.example.anonymous.utils;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.example.anonymous.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;


public class ImgUtil {
    private static final String basicFilePath = "../default_image.png";
    private static final String UPLOAD_FOLDER = "C:\\Users\\yooyj9309\\Desktop\\images";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);

    public static String getRealPath(String savePath, HttpSession session) {
        String sessionFolder = session.getServletContext().getRealPath(savePath);
        LOGGER.info(sessionFolder);
        return sessionFolder;
    }

    public static void makeFolder(String path) {
        File file = new File(path);
        file.mkdirs();
    }

    public static String imgUpload(MultipartFile inputFile, String savedFilePath, HttpSession session) {
        String currentFilePath = basicFilePath;
        String sessionFolder= getRealPath("../images",session);
        String fileName = inputFile.getOriginalFilename();
        MultipartFile savedFile = inputFile;

        UUID uuid = UUID.randomUUID();

        if(!StringUtils.isEmpty(fileName)) {
            currentFilePath = uuid.toString()+"_"+fileName;
            makeFolder(sessionFolder);

            File file = new File(UPLOAD_FOLDER,currentFilePath);
            File tmpFile = new File(sessionFolder,currentFilePath);

            try {
                //inputFile.transferTo(file);
                savedFile.transferTo(tmpFile);
            } catch (IOException e) {
                throw new ServerException("서버 에러입니다.");
            }
        }else{
            if(!StringUtils.isEmpty(savedFilePath)){
                currentFilePath = savedFilePath;
            }
        }
        LOGGER.info(currentFilePath);
        return "../images/"+currentFilePath;
    }


}