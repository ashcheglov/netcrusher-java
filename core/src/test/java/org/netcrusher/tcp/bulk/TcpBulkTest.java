package org.netcrusher.tcp.bulk;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;

public class TcpBulkTest {

    private static final int PORT_SERVER = 10082;

    private static final String HOSTNAME = "127.0.0.1";

    private static final long COUNT = 2 * 1_000_000;

    private TcpBulkServer server;

    @Before
    public void setUp() throws Exception {
        server = new TcpBulkServer(new InetSocketAddress(HOSTNAME, PORT_SERVER), COUNT);
    }

    @After
    public void tearDown() throws Exception {
        if (server != null) {
            server.close();
        }
    }

    @Test
    public void test() throws Exception {
        TcpBulkClient client1 = TcpBulkClient.forAddress("EXT", new InetSocketAddress(HOSTNAME, PORT_SERVER), COUNT);
        client1.await(10000);

        Assert.assertEquals(1, server.getClients().size());
        TcpBulkClient client2 = server.getClients().iterator().next();
        client2.await(10000);

        client1.close();
        client2.close();

        Assert.assertNotNull(client1.getRcvDigest());
        Assert.assertNotNull(client2.getRcvDigest());

        Assert.assertArrayEquals(client1.getRcvDigest(), client2.getSndDigest());
        Assert.assertArrayEquals(client2.getRcvDigest(), client1.getSndDigest());
    }
}
