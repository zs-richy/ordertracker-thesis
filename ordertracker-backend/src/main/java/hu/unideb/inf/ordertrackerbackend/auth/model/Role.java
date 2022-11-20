package hu.unideb.inf.ordertrackerbackend.auth.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

public enum Role {
    USER,
    ADMIN;

}
