package vn.codegym.qlbanhang.controller;

import vn.codegym.qlbanhang.utils.SftpUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;


@WebServlet({"/image/*"})
public class ImageController extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() != null) {
            String imageUrl = req.getPathInfo().substring(1);
            if (imageUrl.isEmpty()) {
                return;
            }
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
}
