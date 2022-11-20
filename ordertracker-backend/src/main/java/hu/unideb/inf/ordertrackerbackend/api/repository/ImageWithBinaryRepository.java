package hu.unideb.inf.ordertrackerbackend.api.repository;

import hu.unideb.inf.ordertrackerbackend.api.enums.ImageType;
import hu.unideb.inf.ordertrackerbackend.api.model.Image;
import hu.unideb.inf.ordertrackerbackend.api.model.ImageWithBinary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ImageWithBinaryRepository extends JpaRepository<ImageWithBinary, Long> {

    @Query("SELECT i FROM ImageWithBinary i WHERE i.product.id = :pId")
    List<ImageWithBinary> findImagesWithBinaryByProductId(@Param("pId") Long id);

    @Query("SELECT i FROM ImageWithBinary i WHERE i.product.id = :pId AND i.type = :iType")
    ImageWithBinary findImagesWithBinaryByProductIdAndType(@Param("pId") Long id, @Param("iType")ImageType imageType);

}
