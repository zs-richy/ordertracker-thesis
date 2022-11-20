package hu.unideb.inf.ordertrackerbackend.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hu.unideb.inf.ordertrackerbackend.api.enums.ImageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorFormula("0")
@DiscriminatorValue("0")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private ImageType type;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
}
