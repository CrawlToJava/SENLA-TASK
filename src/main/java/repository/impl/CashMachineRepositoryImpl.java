package repository.impl;

import entity.CashMachine;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.CashMachineRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CashMachineRepositoryImpl implements CashMachineRepository {
    private final List<CashMachine> cashMachineList = new ArrayList<>();

    @Override
    public void save(CashMachine cashMachine) {
        if (cashMachineList.stream().noneMatch(cashMachine1 -> cashMachine1.getId().equals(cashMachine.getId()))) {
            cashMachineList.add(cashMachine);
        } else {
            throw new NotAvailableException("Банкомат с таким id уже существует");
        }
    }

    @Override
    public void delete(Long id) {
        cashMachineList.remove(findById(id).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует")));
    }

    @Override
    public void update(CashMachine cashMachine) {
        CashMachine updatedCashMachine = findById(cashMachine.getId()).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        updatedCashMachine.setId(cashMachine.getId());
        updatedCashMachine.setCashMachineStatus(cashMachine.getCashMachineStatus());
        updatedCashMachine.setAddress(cashMachine.getAddress());
        updatedCashMachine.setCashMachineMoneyLimit(cashMachine.getCashMachineMoneyLimit());
    }

    @Override
    public Optional<CashMachine> findById(Long id) {
        return cashMachineList.stream().filter(cashMachine -> cashMachine.getId().equals(id)).findFirst();
    }

    @Override
    public List<CashMachine> findAll() {
        return cashMachineList;
    }
}
