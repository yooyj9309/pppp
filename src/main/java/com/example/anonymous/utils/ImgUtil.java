package com.example.anonymous.utils;


import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.example.anonymous.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


public class ImgUtil {
    static final String basicImgPath = "images/제니.png";
    private static final Logger LOGGER = LoggerFactory.getLogger(ImgUtil.class);

    public static String getRealPath(String savePath, HttpSession session) {

        String realPath = session.getServletContext().getRealPath(savePath);
        return realPath;
    }

    public static void makeFolder(String path) {
        File file = new File(path);
        file.mkdirs();
    }

    public static String renameTo(String path, String name) {

        makeFolder(path);

        String fileName = name;
        File file = new File(path, fileName);
        while (file.exists()) {
            UUID uuid = UUID.randomUUID();
            fileName = uuid.toString() + "_" + fileName;
            file = new File(path, fileName);
        }
        return fileName;
    }

    public static String imgUpload(String savePath, HttpSession session, MultipartFile imgfile, String fileimgPath) {
        String imgPath = "";
        try {
            String realPath = ImgUtil.getRealPath(savePath, session);
            String boardImgFileName = imgfile.getOriginalFilename();
            String saveName = "";

            if (!StringUtils.isEmpty(boardImgFileName)) {
                saveName = ImgUtil.renameTo(realPath, boardImgFileName);
                try {
                    File file = new File(realPath, saveName);
                    imgfile.transferTo(file);
                    imgPath =  savePath + "/" + saveName;
                } catch (Exception e) {
                    throw new ServerException("서버 에러입니다.");
                }
            } else {
                if (!StringUtils.isEmpty(fileimgPath)) {
                    imgPath = fileimgPath;
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