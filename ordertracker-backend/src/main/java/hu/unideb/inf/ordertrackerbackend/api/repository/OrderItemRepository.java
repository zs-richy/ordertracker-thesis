package hu.unideb.inf.ordertrackerbackend.api.repository;

import hu.unideb.inf.ordertrackerbackend.api.model.Order;
import hu.unideb.inf.ordertrackerbackend.api.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}