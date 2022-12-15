package application.service.impl;

import application.entity.CashMachine;
import application.repository.CashMachineRepository;
import application.service.CashMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("cashMachineService")
public class CashMachineServiceImpl implements CashMachineService {
    private final CashMachineRepository cashMachineRepository;

    @Autowired
    public CashMachineServiceImpl(CashMachineRepository cashMachineRepository) {
        this.cashMachineRepository = cashMachineRepository;
    }

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
