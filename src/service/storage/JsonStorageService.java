package payment.src.service.storage;

import java.io.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public abstract class JsonStorageService<T> implements StorageService<T> {

    protected static final String BASE_PATH = "payment.src/resources/";

    @Override
    public void save(T entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFileName()))) {
            writer.write(entity.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<T> findFirst() {
        File file = new File(getFileName());
        if (!file.exists())
            return Optional.empty();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(fromString(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public abstract String getFileName();

    public abstract T fromString(String line);
}
