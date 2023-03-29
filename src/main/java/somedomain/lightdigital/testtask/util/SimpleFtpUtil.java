package somedomain.lightdigital.testtask.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;

/**
 * @author Ilya Avkhimenya
 */
@Slf4j
public class SimpleFtpUtil {
    private static final String HOST = "185.27.134.11";
    private static final Integer PORT = 21;
    private static final String USERNAME = "epiz_33891104";
    private static final String PASSWORD = "CLc195rPV8h3cv";

    public static FTPClient getFtpClient() throws IOException {
        FTPClient ftp = new FTPClient();
        ftp.setControlEncoding("UTF-8");
        ftp.connect(HOST, PORT);
        ftp.login(USERNAME, PASSWORD);
        return ftp;
    }
}
