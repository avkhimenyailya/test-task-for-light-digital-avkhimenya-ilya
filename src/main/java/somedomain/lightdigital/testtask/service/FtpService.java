package somedomain.lightdigital.testtask.service;

import org.springframework.stereotype.Service;
import somedomain.lightdigital.testtask.model.FileInfoWrap;

import java.io.File;
import java.util.List;

/**
 * @author Ilya Avkhimenya
 */
@Service
public interface FtpService {

    List<FileInfoWrap> getFileInfoWithFileNamePrefix(String prefix);

    File getFileByFullPath(String path);
}
