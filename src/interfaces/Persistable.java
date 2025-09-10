package interfaces;

/** Simple persistence contract. */
public interface Persistable {
    void saveToFile(String filePath) throws Exception;
    void loadFromFile(String filePath) throws Exception;
}