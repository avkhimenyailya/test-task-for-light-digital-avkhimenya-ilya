package somedomain.lightdigital.testtask.model;

import lombok.Builder;

import java.io.InputStream;

/**
 * @author Ilya Avkhimenya
 */
@Builder
public record FileWrap(
        InputStream inputStream) {
}
