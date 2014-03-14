package org.albertyang2007.graphite.jmeter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.protocol.tcp.sampler.AbstractTCPClient;
import org.apache.jmeter.protocol.tcp.sampler.ReadException;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GraphiteTCPClient implementation. Compare with TCPClientImpl,
 * GraphiteTCPClient has below different: The client will not check
 * the TCP socket response after sending the data. It uses plaintext
 * protocol to send data to Graphite Server.
 * 
 */
public class GraphiteTCPClient extends AbstractTCPClient {
    private static final Logger log = LoggerFactory.getLogger(GraphiteTCPClient.class);

    private String charset = JMeterUtils.getPropDefault("tcp.charset", Charset.defaultCharset().name()); // $NON-NLS-1$

    // default is not in range of a byte

    public GraphiteTCPClient() {
        super();
        setCharset(charset);
        String configuredCharset = JMeterUtils.getProperty("tcp.charset");
        if (StringUtils.isEmpty(configuredCharset)) {
            log.info("Using platform default charset:" + charset);
        } else {
            log.info("Using charset:" + configuredCharset);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void write(OutputStream os, String s) throws IOException {
        long timestamp = System.currentTimeMillis() / 1000;
        String message = "\n" + s.trim() + " " + timestamp + "\n";

        os.write(message.getBytes(charset));
        os.flush();
        if (log.isDebugEnabled()) {
            log.debug("Wrote: " + message);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void write(OutputStream os, InputStream is) throws IOException {
        byte buff[] = new byte[512];
        while (is.read(buff) > 0) {
            os.write(buff);
            os.flush();
        }
    }

    /**
     * Just return empty String if tend to reading the inputstream
     * from TCP socket
     */
    public String read(InputStream is) throws ReadException {
        return "";
    }
}
