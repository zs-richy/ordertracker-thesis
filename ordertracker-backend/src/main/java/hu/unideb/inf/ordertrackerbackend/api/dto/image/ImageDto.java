package hu.unideb.inf.ordertrackerbackend.api.dto.image;

import lombok.Data;

@Data
public class ImageDto {
    private long id;
    private byte[] imageData;
    private long productId;
}
