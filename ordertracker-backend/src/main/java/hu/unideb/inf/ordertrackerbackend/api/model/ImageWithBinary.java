package hu.unideb.inf.ordertrackerbackend.api.model;

import lombok.Data;
import org.springframework.util.Base64Utils;

import javax.persistence.*;
import java.util.Base64;

@Entity
@Table(name = "images")
@Data
@DiscriminatorValue("00")
public class ImageWithBinary extends Image {

    @Lob
    private byte[] imageData;

    public void decodeBase64Data() {
        if (org.apache.tomcat.util.codec.binary.Base64.isBase64(this.imageData)) {
            this.imageData = Base64.getDecoder().decode(this.imageData);
        }
    }

}
