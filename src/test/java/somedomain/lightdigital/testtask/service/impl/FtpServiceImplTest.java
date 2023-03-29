package somedomain.lightdigital.testtask.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import somedomain.lightdigital.testtask.model.FileInfoWrap;
import somedomain.lightdigital.testtask.service.FtpService;

import java.util.List;

/**
 * @author Ilya Avkhimenya
 */
@SpringBootTest
class FtpServiceImplTest {

    @Autowired
    private FtpService ftpService;

    @Test
    void getFilePathsWithPrefix() {
        List<FileInfoWrap> wraps = ftpService.getFileInfoWithFileNamePrefix("GRP327_");
        Assertions.assertNotEquals(wraps.size(), 0);
    }
}