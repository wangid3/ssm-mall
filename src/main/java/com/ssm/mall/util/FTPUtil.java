package com.ssm.mall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class FTPUtil {
    private final static int PORT = 21;//ftp服务端口号是21
    private final static String  ftpServerIP ;
    private final static String username ;
    private final static String password ;
    private final static Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static FTPClient ftpClient;//org.apache.commons.net.ftp.FTPClient
    static{
        //从属性文件中读取ftp服务器配置信息
        ftpServerIP = PropertyUtil.getProperty("ftp.server.ip");
        username = PropertyUtil.getProperty("ftp.username");
        password = PropertyUtil.getProperty("ftp.password");
    }
    /**
     * @param ftpRemotePath ftp服务器上传路径
     * @param fileList 待上传的文件列表
     * @return
     */
    public static boolean upload(String ftpRemotePath, List<File> fileList){
        boolean flag = true;
        //应用ftp服务器
         ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpServerIP,PORT);//连接FTP服务器,端口21
            ftpClient.login(username,password);//使用用户名和密码登录FTP服务器
        } catch (IOException e) {
            flag = false;
            logger.error("ftp服务器连接错误",e);
        }
        if(flag){//如果连接服务器成功
            InputStream in = null;
            try {
                ftpClient.changeWorkingDirectory(ftpRemotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);// 文件类型：二进制
                ftpClient.enterLocalPassiveMode();//而端口的打开中，使用被动模式，使服务器开放端口。
                // 被动模式：服务端开放端口给客户端用。反之为主动模式。由于很多客户端在防火墙内，所以用被动模式比较多。
                for(File file:fileList){
                    in = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),in);
                }
            } catch (IOException e) {
                flag = false;
                logger.error("上传文件异常",e);
            }finally {
                try {
                    if (in != null) in.close();
                    ftpClient.disconnect();
                }catch(Exception e){
                    logger.error("上传功能执行后，资源关闭是发生异常",e);
                }
            }
        }
        return flag;
    }

}
