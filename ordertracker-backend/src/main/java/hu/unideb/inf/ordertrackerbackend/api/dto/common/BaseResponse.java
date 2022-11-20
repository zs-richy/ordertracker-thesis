package hu.unideb.inf.ordertrackerbackend.api.dto.common;

import lombok.Data;

import java.util.ArrayList;

@Data
public abstract class BaseResponse {
    private ArrayList<String> errorMessages = new ArrayList();
}
