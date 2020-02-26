package persistence;

import java.util.Scanner;

public interface CSVSerializable<T> {
    public String serialize();
    public void deserialize(Scanner scanner);
}
