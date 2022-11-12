package repository.impl;

import au.com.bytecode.opencsv.CSVWriter;
import entity.CashMachine;
import entity.CashMachineStatus;
import exception.NoDataFoundException;
import exception.NotAvailableException;
import repository.CashMachineRepository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CashMachineRepositoryImpl implements CashMachineRepository {
    private List<CashMachine> cashMachineList = new ArrayList<>();

    private final String dataBase = "C:\\Users\\User\\IdeaProjects\\SENLA-TECHNICAL-TASK\\src\\main\\java\\data\\CashMachines.csv";

    @Override
    public void save(CashMachine cashMachine) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(dataBase, true))) {
            cashMachineList = findAll();
            if (cashMachineList.stream().noneMatch(cashMachine1 -> cashMachine1.getId().equals(cashMachine.getId()))) {
                cashMachineList.add(cashMachine);
                String[] record = {String.valueOf(cashMachine.getId())
                        , String.valueOf(cashMachine.getAddress())
                        , String.valueOf(cashMachine.getCashMachineMoneyLimit())
                        , String.valueOf(cashMachine.getCashMachineStatus())};
                writer.writeNext(record);
            } else {
                throw new NotAvailableException("Банкомат с таким id уже существует");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        cashMachineList = findAll();
        cashMachineList.remove(findById(id).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует")));
        refreshAfterDeleting(cashMachineList);
    }

    @Override
    public void update(CashMachine cashMachine) {
        CashMachine updatedCashMachine = findById(cashMachine.getId()).orElseThrow(() -> new NoDataFoundException("Банкомата с таким id не существует"));
        updatedCashMachine.setId(cashMachine.getId());
        updatedCashMachine.setCashMachineStatus(cashMachine.getCashMachineStatus());
        updatedCashMachine.setAddress(cashMachine.getAddress());
        updatedCashMachine.setCashMachineMoneyLimit(cashMachine.getCashMachineMoneyLimit());
        delete(cashMachine.getId());
        save(cashMachine);
    }

    @Override
    public Optional<CashMachine> findById(Long id) {
        return findAll().stream().filter(cashMachine -> cashMachine.getId().equals(id)).findFirst();
    }

    @Override
    public List<CashMachine> findAll() {
        List<CashMachine> list = new ArrayList<>();
        Path path = Paths.get(dataBase);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                CashMachine cashMachine = createCashMachine(attributes);
                list.add(cashMachine);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private CashMachine createCashMachine(String[] metadata) {
        Long id = Long.parseLong(metadata[0].replace("\"", ""));
        String address = metadata[1];
        BigDecimal cashMachineMoneyLimit = new BigDecimal(Long.parseLong(metadata[2].replace("\"", "")));
        CashMachineStatus cashMachineStatus = CashMachineStatus.valueOf(metadata[3].replace("\"", ""));
        return new CashMachine(id, address, cashMachineMoneyLimit, cashMachineStatus);
    }

    private void refreshAfterDeleting(List<CashMachine> list) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(dataBase, true));
             PrintWriter writer = new PrintWriter(dataBase)) {
            writer.print("");
            for (CashMachine cashMachine : list) {
                String[] record = {String.valueOf(cashMachine.getId()).replace("\"", "")
                        , cashMachine.getAddress().replace("\"", "")
                        , String.valueOf(cashMachine.getCashMachineMoneyLimit()).replace("\"", "")
                        , String.valueOf(cashMachine.getCashMachineStatus()).replace("\"", "")};
                csvWriter.writeNext(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
