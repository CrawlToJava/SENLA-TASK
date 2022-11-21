package service;

import entity.CashMachine;

import java.util.List;
import java.util.Optional;

public interface CashMachineService {
    /**
     * This method saves the CashMachine entity to the database
     *
     * @param cashMachine the entity we need to save
     */
    void save(CashMachine cashMachine);

    /**
     * This method deletes the CashMachine entity by id from the database
     *
     * @param id of the entity to be checked for containment in the database
     */
    void delete(Long id);

    /**
     * This method updates the CashMachine entity in the database
     *
     * @param cashMachine the entity with which we need to update the entity in the database
     */
    void update(CashMachine cashMachine);

    /**
     * This method finds the CashMachine entity by id in the database
     *
     * @param id of the entity to be checked for containment in the database
     * @return the CashMachine entity wrapped in an optional
     */
    Optional<CashMachine> findById(Long id);

    /**
     * This method finds all Card entities in the database
     *
     * @return the List of CashMachines
     */
    List<CashMachine> findAll();
}
