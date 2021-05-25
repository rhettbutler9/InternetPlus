package com.xxxx.localism.utils;


import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * FastDFS工具类
 */

@Component
public class FastDFSUtils {

    private static Logger logger= LoggerFactory.getLogger(FastDFSUtils.class);

    /**
     * 初始化客户端
     * ClientGlobal.init()      读取配置文件,并初始化对应的属性
     */
    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
            TrackerGroup trackerGroup = ClientGlobal.g_tracker_group;
            logger.info("初始化分布式文件系统服务完成...");
            System.out.println("--------------------"+ClientGlobal.getG_tracker_group());
        } catch (Exception e) {
            logger.error("初始化FastDFS失败",e.getMessage());
        }

    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file){
        String name=file.getOriginalFilename();
        logger.info("文件名"+name);
        //获取
        StorageClient storageClient=null;
        String[] uploadResults=null;
        try {

            storageClient=getStorageClient();
            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0]=new NameValuePair("author");
            System.out.println(name.substring(name.lastIndexOf(".") + 1));
            uploadResults = storageClient.upload_file(file.getBytes(), name.substring(name.lastIndexOf(".") + 1),
                    meta_list);
            System.out.println("---------------"+uploadResults+"-----------------uploadResults");
        } catch (Exception e) {
//            logger.error("上传文件失败",e.getMessage());
            logger.error("上传失败");
        }
        if(null==uploadResults&&null!=storageClient){
            logger.error("上传失败",storageClient.getErrorCode());
        }
        try {
            storageClient.close();
        } catch (IOException e) {
            logger.error("关闭失败");
        }
        ;
        return  uploadResults;
    }

    /**
     * 获取文件信息
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public  static FileInfo getFileInfo(String groupName,String remoteFileName){
        StorageClient storageClient=null;
        try {
            storageClient=getStorageClient();
            return  storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            logger.error("文件信息获取失败",e.getMessage());
        }
        return null;
    }

    /**
     * 下载文件
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName,String remoteFileName){
        StorageClient storageClient=null;
        try {
            storageClient=getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream inputStream = new ByteArrayInputStream(fileByte);
            return  inputStream;
        } catch (Exception e) {
            logger.error("文件下载失败",e.getMessage());
        }
        return null;
    }

    /**
     * 删除文件
     * @param groupName
     * @param remoteFileName
     */
    public static void deleteFile(String groupName,String remoteFileName){
        StorageClient storageClient=null;
        try {
            storageClient=getStorageClient();
            storageClient.delete_file(groupName,remoteFileName);
        } catch (Exception e) {
            logger.error("文件删除失败",e.getMessage());
        }

    }

    /**
     * 获取文件路径
     * @return
     */
    public static String getTrackerUrl()  {
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer=null;
        StorageServer storeStorage=null;
        try {
            trackerServer = trackerClient.getTrackerServer();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            logger.error("文件路径获取失败",e.getMessage());
        }
        return  "http://"+storeStorage.getInetSocketAddress().getHostString()+":8888/";

    }

    /**
     * 生成StorageClient客户端
     * @return
     * @throws IOException
     */
    private static StorageClient getStorageClient() throws  IOException{
        TrackerServer trackerServer=getTrackerServer();

        StorageClient storageClient = new StorageClient(trackerServer, null);
        if(storageClient!=null){
            logger.info("获取StorageClient");
        }
        return storageClient;
    }


    /**
     * 生成tracker服务器
     * @return
     * @throws IOException
     */
    private  static TrackerServer getTrackerServer() throws IOException{
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        if(trackerServer!=null)
        {
            logger.info("获取TrackerServer");
        }
        return  trackerServer;
    }

}

