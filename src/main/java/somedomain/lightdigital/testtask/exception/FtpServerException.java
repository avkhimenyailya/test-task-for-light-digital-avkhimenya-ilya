package somedomain.lightdigital.testtask.exception;

/**
 * @author Ilya Avkhimenya
 */
public class FtpServerException extends RuntimeException {

    public FtpServerException(Exception e) {
        super("Unable to get information from the server, exp: " + e.getMessage());
    }
}
