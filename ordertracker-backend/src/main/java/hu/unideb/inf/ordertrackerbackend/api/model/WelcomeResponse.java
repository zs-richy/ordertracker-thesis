package hu.unideb.inf.ordertrackerbackend.api.model;

import hu.unideb.inf.ordertrackerbackend.api.dto.common.BaseResponse;
import lombok.Data;

@Data
public class WelcomeResponse extends BaseResponse {
    private String message = "";
    private byte[] imageData;

    public void appendMessage(String message) {
        this.message = this.message.concat(message);
    }
}
