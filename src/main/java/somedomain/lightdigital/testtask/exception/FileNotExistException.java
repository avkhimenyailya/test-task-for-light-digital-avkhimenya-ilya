package somedomain.lightdigital.testtask.exception;

/**
 * @author Ilya Avkhimenya
 */
public class FileNotExistException extends RuntimeException {

    public FileNotExistException(String path) {
        super(String.format("File [%s] does not exist on the ftp-server", path));
    }
}
