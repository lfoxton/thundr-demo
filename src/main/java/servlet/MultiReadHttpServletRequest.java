package servlet;

import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;


public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private byte[] rawData;
    private final HttpServletRequest request;

    public MultiReadHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return getServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getRequestBody() throws IOException {
        return IOUtils.toString(getReader());
    }

    private ServletInputStream getServletInputStream() throws IOException {

        if (this.rawData == null) {
            this.rawData = IOUtils.toByteArray(this.request.getReader());
        }

        return new ServletInputStreamImpl(new ByteArrayInputStream(rawData));
    }

    private static class ServletInputStreamImpl extends ServletInputStream {

        private final InputStream stream;

        public ServletInputStreamImpl(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }
    }
}
