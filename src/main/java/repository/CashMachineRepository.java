package repository;

import entity.Card;
import entity.CashMachine;

import java.util.List;
import java.util.Optional;

public interface CashMachineRepository {
    void save(CashMachine cashMachine);

    void delete(Long id);

    void update(CashMachine cashMachine);

    Optional<CashMachine> findById(Long id);

    List<CashMachine> findAll();
}
