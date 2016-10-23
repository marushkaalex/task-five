package com.epam.am.whatacat.web.servlet;

import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@MultipartConfig(maxFileSize = 1024 * 1024) // 1 MB
@WebServlet(name = "UploadServlet", urlPatterns = "/upload/*")
public class UploadServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(UploadServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        if (user.getAvatarUrl() != null) {
            File file = new File(System.getProperty("upload.location") + "/" + user.getAvatarUrl());
            file.delete();
        }

        Part filePart = req.getPart("file");
            String contentType = filePart.getContentType();
        String fileName = System.currentTimeMillis() + "." + contentType.split("/")[1];
        File newFile = new File(System.getProperty("upload.location") + "/" + fileName);
        newFile.mkdirs();
        Files.copy(filePart.getInputStream(), Paths.get(newFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

        user.setAvatarUrl(fileName);
        try (UserService userService = new UserService()) {
            userService.save(user);
        } catch (ServiceException e) {
            throw new ServletException(e);
        }

        LOG.info("File {} has been uploaded", newFile.getPath());

        resp.sendRedirect("/profile");
    }
}
