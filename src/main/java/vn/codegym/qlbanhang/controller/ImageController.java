package vn.codegym.qlbanhang.controller;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import vn.codegym.qlbanhang.utils.SftpUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@WebServlet({"/image/*"})
public class ImageController extends HttpServlet {

    private static final String SFTP_HOST = "159.13.36.51";
    private static final int SFTP_PORT = 22;
    private static final String SFTP_USER = "server";
    private static final String SFTP_PASSWORD = "Sv@123";
    private static final String SFTP_DIR = "/data/public/";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() != null) {
            String imageUrl = req.getPathInfo().substring(1);
            try {
                byte[] imageData = SftpUtils.getFileAsByteArray(imageUrl);
                resp.setContentType("image/jpeg");
                resp.setContentLength(imageData.length);

                OutputStream out = resp.getOutputStream();
                out.write(imageData);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to retrieve image");
            }
        }
    }

//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        if (request.getPathInfo() != null) {
//            final Part filePart = request.getPart("image");
//            final String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
//
//            // Save the file temporarily
//            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
//            try (InputStream fileContent = filePart.getInputStream()) {
//                Files.copy(fileContent, tempFile.toPath());
//            }
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
//            String dateStr = dateFormat.format(new Date());
//            String sftpDir = SFTP_DIR + "image_" + dateStr + "/";
//
//            // Upload the file to SFTP server
//            JSch jsch = new JSch();
//            Session session = null;
//            ChannelSftp channelSftp = null;
//
//            try {
//                session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
//                session.setPassword(SFTP_PASSWORD);
//                session.setConfig("StrictHostKeyChecking", "no");
//                session.connect();
//
//                channelSftp = (ChannelSftp) session.openChannel("sftp");
//                channelSftp.connect();
//
//                try {
//                    channelSftp.cd(sftpDir);
//                } catch (Exception e) {
//                    String[] folders = sftpDir.split("/");
//                    String path = "";
//                    for (String folder : folders) {
//                        if (folder.length() > 0) {
//                            path += "/" + folder;
//                            try {
//                                channelSftp.cd(path);
//                            } catch (Exception ex) {
//                                channelSftp.mkdir(path);
//                                channelSftp.cd(path);
//                            }
//                        }
//                    }
//                }
//
//                // Upload file to SFTP server
//                try (FileInputStream fis = new FileInputStream(tempFile)) {
//                    channelSftp.put(fis, SFTP_DIR + fileName);
//                }
//
//                response.getWriter().println("File uploaded successfully to SFTP server: " + SFTP_DIR + fileName);
//            } catch (Exception e) {
//                throw new ServletException("Error uploading file to SFTP server", e);
//            } finally {
//                if (channelSftp != null && channelSftp.isConnected()) {
//                    channelSftp.disconnect();
//                }
//                if (session != null && session.isConnected()) {
//                    session.disconnect();
//                }
//                // Delete the temporary file
//                tempFile.delete();
//            }
//        }
//    }


    public String getPathSFTP(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String pathFinal = "";
        final Part filePart = request.getPart("image");
        final String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Save the file temporarily
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, tempFile.toPath());
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String dateStr = dateFormat.format(new Date());
        String sftpDir = SFTP_DIR + "image_" + dateStr + "/";

        // Upload the file to SFTP server
        JSch jsch = new JSch();
        Session session = null;
        ChannelSftp channelSftp = null;

        try {
            session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);
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
                    if (folder.length() > 0) {
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
