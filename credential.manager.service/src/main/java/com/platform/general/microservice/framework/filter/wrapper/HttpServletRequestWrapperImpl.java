package com.platform.general.microservice.framework.filter.wrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class HttpServletRequestWrapperImpl extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpServletRequestWrapperImpl.class);

    private final String requestBody;

    public HttpServletRequestWrapperImpl(HttpServletRequest request){
        super(request);
        requestBody = readRequestBody(request);
    }

    private String readRequestBody(final HttpServletRequest request){

        String requestBodyTMP;
        StringBuilder requestBodyBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try(InputStream inputStream = request.getInputStream()) {
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    requestBodyBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ioException) {
            logger.error("There is a problem reading information from request [inputStream]",ioException);
        } finally {
            requestBodyTMP = requestBodyBuilder.toString();

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ioException) {
                    logger.error("There is a problem closing buffer of request",ioException);
                }
            }
        }
        return requestBodyTMP;
    }

    @Override
    public ServletInputStream getInputStream () {
        return new ServletInputStreamImpl(new ByteArrayInputStream(requestBody.getBytes()));
    }

}

