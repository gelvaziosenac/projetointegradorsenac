package bancocrudspringboot.repository;

import bancocrudspringboot.model.Credito;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Long>{
    Credito findCreditoByUsuario(Long credito);
}