package com.gateweb.charge.component.annotated;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Deprecated
@Component
public class InputAndOutputStreamUtils {

    public void writeToResponse(Resource resource, HttpServletResponse response, String fileName) throws IOException {
        // gets MIME type of the file
        String mimeType = Files.probeContentType(Paths.get(resource.getFile().getPath()));
        response.setContentType(mimeType);
        response.setContentLength((int) resource.getFile().length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=", fileName);
        response.setHeader(headerKey, headerValue);
        InputStream inputStream = resource.getInputStream();
        ServletOutputStream out = response.getOutputStream();

        byte[] buffer = new byte[1048];

        int numBytesRead;
        while ((numBytesRead = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, numBytesRead);
        }

    }
}