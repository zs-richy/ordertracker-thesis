package hu.unideb.inf.ordertrackerbackend.api.repository;

import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.id = ?1")
    Product findByIdWithoutImages(Long id);

    @Query("SELECT p FROM Product p WHERE p.isVisible = true")
    List<Product> findVisibleProducts();

    @Modifying
    @Query("UPDATE Product p SET p.isVisible = " +
            "CASE p.isVisible" +
            "   WHEN TRUE THEN FALSE" +
            "   ELSE TRUE END "+
            "WHERE p.id = :pId")
    void changeVisibilityById(@Param("pId") Long productId);
}
