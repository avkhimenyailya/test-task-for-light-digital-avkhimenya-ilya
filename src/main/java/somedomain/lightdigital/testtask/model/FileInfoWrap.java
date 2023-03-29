package somedomain.lightdigital.testtask.model;

import lombok.Builder;

/**
 * @author Ilya Avkhimenya
 */
@Builder
public record FileInfoWrap(
        String name,
        String creationDate,
        String sizeInMB,
        String apiLink) {
}
