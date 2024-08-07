package vn.codegym.qlbanhang.utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;


public class SftpUtils {
    //    private static final String SFTP_HOST = "localhost";
    private static final String SFTP_HOST = "150.230.9.133";
    private static final String SFTP_USERNAME = "server";
    private static final String SFTP_PASSWORD = "Sv@123";
    private static final int SFTP_PORT = 22;

    private static Session setupJsch() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SFTP_USERNAME, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASSWORD);

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect(30000);
        return session;
    }

    public static byte[] getFileAsByteArray(String remoteFile) throws Exception {
        Session session = setupJsch();
        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect(30000);
        InputStream inputStream = channelSftp.get(remoteFile);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        channelSftp.disconnect();
        session.disconnect();

        return byteArrayOutputStream.toByteArray();
    }

    public static void getFile(Map<String, String> map) {
        try {
            Session session = setupJsch();
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            for (Map.Entry<String, String> data : map.entrySet()) {
                try {
                    InputStream inputStream = channelSftp.get(data.getKey());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, bytesRead);
                    }
                    inputStream.close();
                    data.setValue(Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray()));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            channelSftp.disconnect();
            session.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
