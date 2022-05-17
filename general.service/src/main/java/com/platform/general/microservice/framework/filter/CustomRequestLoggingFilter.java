package com.platform.general.microservice.framework.filter;

import com.platform.general.microservice.framework.filter.wrapper.HttpServletRequestWrapperImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

public class CustomRequestLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(CustomRequestLoggingFilter.class);

    public void init(FilterConfig config) {
    }

    /***
     * Main filter method.
     *
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     *
     */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequestWrapperImpl requestWrapper = new HttpServletRequestWrapperImpl((HttpServletRequest) servletRequest);

        //The initial time.
        long startMilliSeconds = Calendar.getInstance().getTimeInMillis();

        filterChain.doFilter(requestWrapper, servletResponse);

        //The spend time
        Long spendTime = Calendar.getInstance().getTimeInMillis() - startMilliSeconds;

        logger.debug("Method:{}, URI:{}, Elapsed Time:{}ms, Status:{}, Client:{}, User:{}, {}",requestWrapper.getMethod(),requestWrapper.getRequestURI(),spendTime,((HttpServletResponse)servletResponse).getStatus(),requestWrapper.getRemoteAddr(),requestWrapper.getRemoteUser());
    }
}
