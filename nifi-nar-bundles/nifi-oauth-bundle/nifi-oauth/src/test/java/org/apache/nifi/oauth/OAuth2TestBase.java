package org.apache.nifi.oauth;

import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.ssl.SSLContextService;
import org.apache.nifi.ssl.StandardSSLContextService;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.apache.nifi.web.util.TestServer;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.AfterClass;
import org.junit.Before;

import javax.net.ssl.HttpsURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OAuth2TestBase {
    protected static TestServer secureServer;
    protected static TestServer unsecureServer;
    private static final String ID_CONTROLLER_SERVICE_SSL_CONTEXT = "ssl-context";

    private TestRunner runner;

    public static void initServer(List<ServletHandler> secureHandlers,
                                  List<ServletHandler> unsecureHandlers) throws Exception {
        // create the secure server
        secureServer = new TestServer(createSSLProperties());
        secureServer.clearHandlers();
        if (secureHandlers != null) {
            for (ServletHandler curHandler : secureHandlers) {
                secureServer.addHandler(curHandler);
            }
        }
        secureServer.startServer();

        // create the non-secure server
        unsecureServer = new TestServer();
        unsecureServer.clearHandlers();
        if (unsecureHandlers != null) {
            for (ServletHandler curHandler : unsecureHandlers) {
                unsecureServer.addHandler(curHandler);
            }
        }
        unsecureServer.startServer();

        // wait for server to initialize
        Thread.sleep(1000);
    }

    @Before
    public void init() throws InitializationException, InterruptedException {
        runner = TestRunners.newTestRunner(SampleProcessor.class);

        final StandardSSLContextService sslService = new StandardSSLContextService();
        runner.addControllerService(ID_CONTROLLER_SERVICE_SSL_CONTEXT, sslService, createSSLProperties());
        runner.enableControllerService(sslService);

        // allow time for the controller service to fully initialize
        Thread.sleep(500);
    }

    public static Map<String, String> createSSLProperties() {
        final Map<String, String> map = new HashMap<>();
        map.put(StandardSSLContextService.KEYSTORE.getName(), "src/test/resources/localhost-ks.jks");
        map.put(StandardSSLContextService.KEYSTORE_PASSWORD.getName(), "localtest");
        map.put(StandardSSLContextService.KEYSTORE_TYPE.getName(), "JKS");
        map.put(StandardSSLContextService.TRUSTSTORE.getName(), "src/test/resources/localhost-ts.jks");
        map.put(StandardSSLContextService.TRUSTSTORE_PASSWORD.getName(), "localtest");
        map.put(StandardSSLContextService.TRUSTSTORE_TYPE.getName(), "JKS");
        return map;
    }

    protected void setDefaultSSLSocketFactory(TestRunner runner, String sslContextServiceId,
                                              SSLContextService.ClientAuth clientAuth) {
        StandardSSLContextService sslContextService =
                (StandardSSLContextService) runner.getControllerService(sslContextServiceId);
        HttpsURLConnection
                .setDefaultSSLSocketFactory(sslContextService.createSSLContext(clientAuth).getSocketFactory());
    }

    protected void setDefaultSSLSocketFactory() {
        setDefaultSSLSocketFactory(runner, ID_CONTROLLER_SERVICE_SSL_CONTEXT, SSLContextService.ClientAuth.REQUIRED);
    }

    @AfterClass
    public static void deinitServer() throws Exception {
        if (secureServer != null) secureServer.shutdownServer();
        if (unsecureServer != null) unsecureServer.shutdownServer();
    }

    public static class SampleProcessor extends AbstractProcessor {
        @Override
        public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {

        }
    }

}
