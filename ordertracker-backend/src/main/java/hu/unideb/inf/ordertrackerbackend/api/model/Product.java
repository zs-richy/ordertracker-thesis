package hu.unideb.inf.ordertrackerbackend.api.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private float price;

    @Column(name = "isvisible")
    private Boolean isVisible = true;

    @OneToMany(mappedBy = "product")
    private Set<Image> images;

    public void addImage(Image image) {
        this.images.add(image);
    }

}
