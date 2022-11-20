package hu.unideb.inf.ordertrackerbackend.api.repository;

import hu.unideb.inf.ordertrackerbackend.api.enums.OrderStatus;
import hu.unideb.inf.ordertrackerbackend.api.model.Order;
import hu.unideb.inf.ordertrackerbackend.api.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems.order", "orderItems.product"})
    @Override
    List<Order> findAll();

    @Modifying
    @Query("UPDATE Order SET status = :newStatus WHERE id = :oId")
    void updateStatusById(@Param("oId") Long orderId, @Param("newStatus") OrderStatus newStatus);

    @Modifying
    @Query("UPDATE Order SET statusLastModify = :modifyDate WHERE id = :oId")
    void updateStatusModifyById(@Param("oId") Long orderId, @Param("modifyDate") LocalDateTime newModifyDate);

    @EntityGraph(attributePaths = {"orderItems.order", "orderItems.product"})
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :oDateFrom AND :oDateTo")
    List<Order> findAllByDate(@Param("oDateFrom") LocalDateTime orderDateFrom, @Param("oDateTo") LocalDateTime orderDateTo);

    @EntityGraph(attributePaths = {"orderItems.order", "orderItems.product"})
    @Query("SELECT o FROM Order o WHERE o.userId = :oUserId")
    List<Order> findAllByUserId(@Param("oUserId") Long userId);
}