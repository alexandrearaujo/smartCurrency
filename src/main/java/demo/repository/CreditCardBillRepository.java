package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.CreditCardBill;

@Repository
public interface CreditCardBillRepository extends JpaRepository<CreditCardBill, Long> {

}
