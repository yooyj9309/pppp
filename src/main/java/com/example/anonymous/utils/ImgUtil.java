package com.example.anonymous.utils;


import java.io.File;
import java.util.UUID;

import com.example.anonymous.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;



public class ImgUtil {
    private final static String basicImgPath = "../images/default_image.png";
    private final static String REAL_PATH = "C:\\Users\\yooyj9309\\IdeaProjects\\anonymousboard\\src\\main\\webapp\\images";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);


    public static void makeFolder(String path) {
        File file = new File(path);
        file.mkdirs();
    }

    public static String renameTo(String path, String name) {

        makeFolder(path);

        String fileName = name;
        File file = new File(path, fileName);

        //UUID를 하더라도 중복이 존재할 수 있으므로
        while (file.exists()) {
            UUID uuid = UUID.randomUUID();
            fileName = uuid.toString() + "_" + fileName;
            file = new File(path, fileName);
        }
        return fileName;
    }

    public static String imgUpload(String savePath, MultipartFile inputFile, String filePath) {
        String imgPath = "";
        try {
            String imageFileName = inputFile.getOriginalFilename();
            String saveName = "";

            if (!StringUtils.isEmpty(imageFileName)) {
                saveName = ImgUtil.renameTo(REAL_PATH, imageFileName);
                try {
                    File file = new File(REAL_PATH, saveName);
                    inputFile.transferTo(file);
                    imgPath = "../" + savePath + "/" + saveName;

                    LOGGER.info("이미지 업로드 성공");
                } catch (Exception e) {
                    throw new ServerException("서버 에러입니다.");
                }
            } else {
                if (!StringUtils.isEmpty(filePath)) {
                    imgPath = filePath;
                } else {
                    imgPath = basicImgPath;
                }
            }
        } catch (Exception e) {
            imgPath = basicImgPath;
        }
        return imgPath;
    }

}