package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    public String upload(MultipartFile file,String path){
        String filename=file.getOriginalFilename();
        //获取文件扩展名
        String fileExtensionName=filename.substring(filename.lastIndexOf(".")+1);
        String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件名为:{},上传路径为:{},新文件名为:{}",file,path,uploadFileName);
        File fileDir=new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName);
        try {
            file.transferTo(targetFile);
            //将targetFile上传到ftp服务器上
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //上传完成后删除upload下面的文件
            targetFile.delete();
        } catch (IOException e) {
            logger.error("上传文件异常");
            return null;
        }
        return targetFile.getName();
    }
}
