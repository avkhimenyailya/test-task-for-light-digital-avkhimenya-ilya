package somedomain.lightdigital.testtask.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import somedomain.lightdigital.testtask.model.FileInfoWrap;
import somedomain.lightdigital.testtask.service.FtpService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * @author Ilya Avkhimenya
 */
@RestController
@RequestMapping("/api/v1/ftp")
@RequiredArgsConstructor
public class FtpController {
    private final FtpService ftpService;

    @GetMapping("/list")
    public ResponseEntity<List<FileInfoWrap>> getAllFilePathsByPrefixFromFtpServer(
            @RequestParam(required = false) String prefix) {
        List<FileInfoWrap> fileInfoList = ftpService.getFileInfoWithFileNamePrefix(prefix);
        return ResponseEntity.ok().body(fileInfoList);
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> getFileFromFtpServerByPath(
            @RequestParam String path, HttpServletRequest request) throws IOException {
        File file = ftpService.getFileByFullPath(path);
        String mimeType = Files.probeContentType(file.toPath());
        InputStreamResource inputStreamResource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity
                .ok()
                .contentType(MediaType.asMediaType(MimeType.valueOf(mimeType)))
                .body(inputStreamResource);
    }
}
