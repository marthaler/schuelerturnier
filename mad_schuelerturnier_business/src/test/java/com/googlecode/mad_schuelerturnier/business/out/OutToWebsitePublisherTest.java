package com.googlecode.mad_schuelerturnier.business.out;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class OutToWebsitePublisherTest {

    private FakeFtpServer fakeFtpServer;

    @Before
    public void setup() {
        this.fakeFtpServer = new FakeFtpServer();

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new FileEntry("/test.txt", "hallo"));
        this.fakeFtpServer.setFileSystem(fileSystem);
        // Create UserAccount with username, password, home-directory
        final UserAccount userAccount = new UserAccount("test", "test", "/");
        this.fakeFtpServer.addUserAccount(userAccount);
        this.fakeFtpServer.setServerControlPort(2222);
        this.fakeFtpServer.start();
    }

    @After
    public void teardown() {
        this.fakeFtpServer.stop();
    }

    @Test
    public void testInitialisation() throws Exception {

        final OutToWebsitePublisher pub = new OutToWebsitePublisher();

        pub.addPage("pagea.html", "contenta");
        pub.addPage("pageb.html", "contentb");

        pub.publish();

        Thread.sleep(1000);

        final List<FileEntry> files = this.fakeFtpServer.getFileSystem().listFiles("/");
        for (final Object file : files) {

            if (file instanceof org.mockftpserver.fake.filesystem.DirectoryEntry) {
                continue;
            }
            final FileEntry temp = (FileEntry) file;

            Thread.sleep(1000);
            final InputStream in = temp.createInputStream();
            final StringBuffer buff = new StringBuffer();
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buff.append(line);
            }
            final String content = buff.toString();
            if (temp.getName().contains("page")) {
                Assert.assertTrue(content.contains("content"));
            }
        }
    }
}
