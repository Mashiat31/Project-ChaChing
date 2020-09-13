package persistence;

import java.util.Scanner;

@SuppressWarnings("checkstyle:TypeName")
public interface Saveable<T> {
    public String serialize();

    public void deserialize(Scanner scanner);
}
