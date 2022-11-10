package service.impl;

import entity.CashMachine;
import repository.CashMachineRepository;
import service.CashMachineService;

import java.util.List;
import java.util.Optional;

public class CashMachineServiceImpl implements CashMachineService {
    private CashMachineRepository cashMachineRepository;

    @Override
    public void save(CashMachine cashMachine) {
        cashMachineRepository.save(cashMachine);
    }

    @Override
    public void delete(Long id) {
        cashMachineRepository.delete(id);
    }

    @Override
    public void update(CashMachine cashMachine) {
        cashMachineRepository.update(cashMachine);
    }

    @Override
    public Optional<CashMachine> findById(Long id) {
        return cashMachineRepository.findById(id);
    }

    @Override
    public List<CashMachine> findAll() {
        return cashMachineRepository.findAll();
    }
}
