package main.service.storage;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import main.domain.IdentificationEntity;

public abstract class TxtStorageService<T extends IdentificationEntity> implements StorageService<T> {

    private static final String BASE_PATH = "src/main/resources/";

    @Override
    public void save(T entity) {
        if (entity.getId() != 0) {
            throw new RuntimeException("Cannot create an entity that already has an ID");
        }

        // Get sequence
        int sequence = 1;
        try (BufferedReader reader = new BufferedReader(new FileReader(getSequenceFileName()))) {
            sequence = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save entity
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(), true))) {
            entity.setId(sequence);
            writer.write(entity.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update sequence
        try (BufferedWriter sequenceWriter = new BufferedWriter(new FileWriter(getSequenceFileName()))) {
            sequenceWriter.write(String.valueOf(sequence + 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<T> findByIds(List<Integer> ids) {
        File file = new File(getFileName());
        List<T> entities = new LinkedList<>();
        if (!file.exists())
            return Collections.emptyList();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T bill = fromString(line);
                if (ids.contains(bill.getId())) {
                    entities.add(bill);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    @Override
    public Optional<T> findFirst() {
        File file = new File(getFileName());
        if (!file.exists())
            return Optional.empty();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read first record
            String line = reader.readLine();
            if (line != null) {
                return Optional.of(fromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        File file = new File(getFileName());
        if (!file.exists()) {
            return Collections.emptyList();
        }

        List<T> list = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read all records
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(fromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(T entity) {
        List<T> list = findAll();
        list.stream().filter(element -> element.getId() == entity.getId()).findFirst()
                .ifPresent(element -> list.set(list.indexOf(element), entity));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName()))) {
            for (T t : list) {
                writer.write(t.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getEntityName();

    protected abstract T fromString(String line);

    private String getFileName() {
        return BASE_PATH + getEntityName() + ".txt";
    }

    private String getSequenceFileName() {
        return BASE_PATH + getEntityName() + "_sequence.txt";
    }
}
