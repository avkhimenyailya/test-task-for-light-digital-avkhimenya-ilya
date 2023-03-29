package somedomain.lightdigital.testtask.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import somedomain.lightdigital.testtask.exception.FileNotExistException;
import somedomain.lightdigital.testtask.exception.FtpServerException;
import somedomain.lightdigital.testtask.model.FileInfoWrap;
import somedomain.lightdigital.testtask.service.FtpService;
import somedomain.lightdigital.testtask.util.SimpleFtpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ilya Avkhimenya
 */
@Slf4j
@Service
public class FtpServiceImpl implements FtpService {

    @Override
    public List<FileInfoWrap> getFileInfoWithFileNamePrefix(String prefix) {
        try {
            FTPClient ftpClient = SimpleFtpUtil.getFtpClient();
            List<FTPFileWrap> fileAtFtpServer = findFileAtFtpServer(ftpClient, prefix);
            return fileAtFtpServer
                    .stream()
                    .map(this::createInfoWrapByFtpFile)
                    .toList();
        } catch (IOException e) {
            throw new FtpServerException(e);
        }
    }

    @Override
    public File getFileByFullPath(String path) {
        try {
            FTPClient ftpClient = SimpleFtpUtil.getFtpClient();
            path = StringUtils.replace(path, "qu21W", " ");
            InputStream is = ftpClient.retrieveFileStream(path);
            if (is != null) {
                String extension = FilenameUtils.getExtension(path);
                try {
                    File temp = File.createTempFile("tmp", "." + extension);
                    temp.deleteOnExit();
                    FileOutputStream fos = new FileOutputStream(temp);
                    IOUtils.copy(is, fos);
                    return temp;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new FileNotExistException(path);
            }
        } catch (IOException e) {
            throw new FtpServerException(e);
        }
    }

    private List<FTPFileWrap> findFileAtFtpServer(FTPClient ftpClient, String prefix)
            throws IOException {
        List<FTPFileWrap> resultList = new ArrayList<>();
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for (FTPFile ftpFile : ftpFiles) {
            if (!ftpFile.getName().startsWith(".")) {
                if (ftpFile.isDirectory()) {
                    ftpClient.changeWorkingDirectory(ftpFile.getName());
                    resultList.addAll(findFileAtFtpServer(ftpClient, prefix));
                    ftpClient.changeToParentDirectory();
                    continue;
                }
                if (prefix == null || ftpFile.getName().startsWith(prefix)) {
                    if (isValidFile(ftpFile.getName())) {
                        resultList.add(new FTPFileWrap(ftpFile, ftpClient.printWorkingDirectory()));
                    }
                }
            }
        }
        return resultList;
    }

    private FileInfoWrap createInfoWrapByFtpFile(FTPFileWrap ftpFileWrap) {
        String fullName = ftpFileWrap.workingDirectory
                .concat(ftpFileWrap.workingDirectory.equals("/") ? "" : "/")
                .concat(ftpFileWrap.ftpFile.getName());

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.systemDefault());
        String createDate = formatter.format(ftpFileWrap.ftpFile.getTimestamp().toInstant());

        Double sizeInMB = ftpFileWrap.ftpFile.getSize() / 1000000D;
        return FileInfoWrap
                .builder()
                .name(ftpFileWrap.ftpFile.getName())
                .creationDate(createDate)
                .sizeInMB(String.format("%.2f", sizeInMB))
                .apiLink("/api/v1/ftp?path=" + StringUtils.replace(fullName, " ", "qu21W"))
                .build();
    }

    private boolean isValidFile(String fileName) {
        String[] extension = {"jpg", "png", "gif"}; // пока только картинки
        String[] split = fileName.split("\\.");
        if (split.length == 0) return false;
        return Arrays.asList(extension).contains(split[split.length - 1]);
    }

    @Data
    @AllArgsConstructor
    private static class FTPFileWrap {
        private FTPFile ftpFile;
        private String workingDirectory;
    }
}
