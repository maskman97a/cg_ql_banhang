package vn.codegym.qlbanhang.utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Properties;


public class SftpUtils {

    private static final String SFTP_HOST = "34.87.23.65";
    private static final int SFTP_PORT = 22;

    private static final String SFTP_USER_SERVER = "server";
    private static final String SFTP_PASSWORD_SERVER = "Sv@123";
    private static final String SFTP_DIR_SERVER = "/data/public/";

    private static Session setupJsch() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(SFTP_USER_SERVER, SFTP_HOST, SFTP_PORT);
        session.setPassword(SFTP_PASSWORD_SERVER);

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


    public static String getPathSFTP(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyMMddHHmmss");
        String strDate = formatDate.format(new Date());
        final Part filePart = request.getPart("file");
        String oldFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String extension = oldFileName.substring(oldFileName.lastIndexOf('.') + 1);
        final String fileName = oldFileName.substring(0, oldFileName.lastIndexOf('.')) + "_" + strDate + "." + extension;
        // Save the file temporarily
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, tempFile.toPath());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String dateStr = dateFormat.format(new Date());
        String sftpDir = SFTP_DIR_SERVER + "image_" + dateStr + "/";

        // Upload the file to SFTP server
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(SFTP_USER_SERVER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD_SERVER);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            try {
                channelSftp.cd(sftpDir);
            } catch (Exception e) {
                String[] folders = sftpDir.split("/");
                String path = "";
                for (String folder : folders) {
                    if (!folder.isEmpty()) {
                        path += "/" + folder;
                        try {
                            channelSftp.cd(path);
                        } catch (Exception ex) {
                            channelSftp.mkdir(path);
                            channelSftp.cd(path);
                        }
                    }
                }
            }

            // Upload file to SFTP server
            try (FileInputStream fis = new FileInputStream(tempFile)) {
                channelSftp.put(fis, sftpDir + fileName);
            }
            response.getWriter().println("File uploaded successfully to SFTP server: " + sftpDir + fileName);
            return sftpDir + fileName;
        } catch (Exception e) {
            throw new ServletException("Error uploading file to SFTP server", e);
        } finally {
            if (channelSftp != null && channelSftp.isConnected()) {
                channelSftp.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
            // Delete the temporary file
            tempFile.delete();
        }
    }
}
