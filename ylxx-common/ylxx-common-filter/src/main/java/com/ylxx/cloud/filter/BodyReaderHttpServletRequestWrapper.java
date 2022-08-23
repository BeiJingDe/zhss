package com.ylxx.cloud.filter;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * @description: body可重复读取
 * @author: caixiaopeng
 * @since: 2021-10-15 09:28:40
 */
@Slf4j
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper
{

    private final byte[] body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException
    {
        super(request);
        this.body = IoUtil.readBytes(request.getInputStream());
    }

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request, byte[] body) throws IOException
    {
        super(request);
        this.body = body;
    }

    /**
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static BodyReaderHttpServletRequestWrapper convert(HttpServletRequest request) throws IOException {
        if(request instanceof BodyReaderHttpServletRequestWrapper) {
            return (BodyReaderHttpServletRequestWrapper) request;
        }
        return new BodyReaderHttpServletRequestWrapper(request);
    }

    @Override
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {
        final ByteArrayInputStream is = new ByteArrayInputStream(body);

        return new ServletInputStream(){

            @Override
            public boolean isFinished()
            {
                return false;
            }

            @Override
            public boolean isReady()
            {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener)
            {

            }

            @Override
            public int read() throws IOException
            {
                return is.read();
            }

        };

    }

    @Override
    public String getHeader(String name)
    {
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames()
    {
        return super.getHeaderNames();
    }

    @Override
    public Enumeration<String> getHeaders(String name)
    {
        return super.getHeaders(name);
    }

}