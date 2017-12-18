package servlet;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] rawData;
    private HttpServletRequest request;
    private MultiReadInputStream servletStream;

    public MultiReadHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new MultiReadInputStream();
    }

    public void resetInputStream() {
        if (rawData != null) {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return new BufferedReader(new InputStreamReader(servletStream));
    }


    private class MultiReadInputStream extends ServletInputStream {

        private InputStream stream;

        @Override
        public int read() throws IOException {
            return stream.read();
        }
    }
}
