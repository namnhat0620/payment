package src.service.storage;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import src.domain.IdentificationEntity;

public abstract class TxtStorageService<T extends IdentificationEntity> implements StorageService<T> {

    protected static final String BASE_PATH = "src/resources/";
    private int sequence = 10;

    @Override
    public void save(T entity) {
        if (isExisted(entity)) {
            return;
        }

        // TODO: Implement sequence generator (fileName = get(fileName) +
        // "_sequence.txt")

        // Save entity
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName(), true))) {
            entity.setId(sequence);
            writer.write(entity.toString());
            writer.newLine();
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

            // Skip first line (sequence)
            reader.readLine();

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
            // Skip first line (sequence)
            reader.readLine();

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
            // Skip first line (sequence)
            reader.readLine();

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
        int nextSequence = list.stream().mapToInt(T::getId).max().orElse(0) + 1;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName()))) {
            writer.write(String.valueOf(nextSequence));
            writer.newLine();
            for (T t : list) {
                writer.write(t.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract String getFileName();

    protected abstract T fromString(String line);

    private boolean isExisted(T entity) {
        List<T> list = findAll();
        return list.stream().anyMatch(e -> e.getId() == entity.getId());
    }
}
