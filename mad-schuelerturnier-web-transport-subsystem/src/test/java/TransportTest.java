
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;

public class TransportTest {

    private TransportTestTomcat embeddedServer;

    @Before
    public void startServer() throws Exception {
        System.setProperty("ownConnectionString","http://localhost:8081/app/transport");

        embeddedServer = new TransportTestTomcat(8081, "/","1");
        embeddedServer.start();

        Thread.sleep(4000);

        System.setProperty("ownConnectionString","http://localhost:8082/app/transport");
        System.setProperty("remoteConnectionString","http://localhost:8081/app/transport");
        embeddedServer = new TransportTestTomcat(8082, "/","2");
        embeddedServer.start();

        Thread.sleep(4000);

        System.setProperty("ownConnectionString","http://localhost:8083/app/transport");
        System.setProperty("remoteConnectionString","http://localhost:8082/app/transport");
        embeddedServer = new TransportTestTomcat(8083, "/","3");
        embeddedServer.start();

        Thread.sleep(4000);

        System.setProperty("ownConnectionString","http://localhost:8084/app/transport");
        System.setProperty("remoteConnectionString","http://localhost:8082/app/transport");
        embeddedServer = new TransportTestTomcat(8084, "/","4");
        embeddedServer.start();

        Thread.sleep(4000);

    }
 
    @After
    public void stopServer() {
        embeddedServer.stop();
    }

    @Test
    public void testSuite (){
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}