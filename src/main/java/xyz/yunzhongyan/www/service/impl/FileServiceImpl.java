package xyz.yunzhongyan.www.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import xyz.yunzhongyan.www.util.Constants;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Component
@Slf4j
public class FileServiceImpl {

    public String upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        try {
            if (file == null || file.isEmpty()) return "";
            String fileName = file.getOriginalFilename();
            log.info("文件名"+fileName);
            //String suffixName = fileName.substring(fileName.lastIndexOf("."));
            String filePath = Constants.IMAGE_PATH;
            //fileName = UUID.randomUUID() + suffixName;
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists())
                dest.getParentFile().mkdirs();
            file.transferTo(dest);
            return Constants.IMAGE_SERVER + fileName;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            log.error("保存文件失败: {}", e);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public List<String> uploads(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        List<String> list = new ArrayList<>();
        try {
            for (String s : fileMap.keySet()) {
                MultipartFile file = fileMap.get(s);
                if (file == null || file.isEmpty()) continue;
                String fileName = file.getOriginalFilename();
                String suffixName = fileName.substring(fileName.lastIndexOf("."));
                String filePath = Constants.IMAGE_PATH;
                fileName = UUID.randomUUID() + suffixName;
                File dest = new File(filePath + fileName);
                if (!dest.getParentFile().exists())
                    dest.getParentFile().mkdirs();
                file.transferTo(dest);
                list.add(Constants.IMAGE_SERVER + fileName);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            log.error("保存文件失败: {}", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
