package hu.unideb.inf.ordertrackerbackend.api.repository;

import hu.unideb.inf.ordertrackerbackend.api.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
