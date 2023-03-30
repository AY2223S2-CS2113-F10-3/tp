package seedu.meal360.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonDatabaseHelper<T> {

    private final String databaseFilepath;
    private final T defaultObject;
    private final Class<T> objectType;
    private final Gson gson;

    public JsonDatabaseHelper(String databaseFilepath, T defaultObject, Class<T> objectType) {
        this.databaseFilepath = databaseFilepath;
        this.defaultObject = defaultObject;
        this.objectType = objectType;

        // Register the LocalDateTimeJsonAdapter and LocalDateJsonAdapter with Gson
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                        LocalDateTimeJsonAdapter.INSTANCE)
                .registerTypeAdapter(LocalDate.class, LocalDateJsonAdapter.INSTANCE).create();
    }

    public T loadDatabase() throws IOException {
        T database;
        Gson gson = new Gson();

        // Check if the JSON file exists and create one if it does not
        File file = new File(databaseFilepath);
        if (!file.exists()) {
            try {
                // Create the parent directory if it does not exist
                File parentDir = file.getParentFile();
                if (!parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                throw new IOException("Error creating database file.");
            }

            // Return a default object if database file does not exist
            return defaultObject;
        }

        // Read the JSON data from the file if it already exists
        try {
            // Read the JSON data from the file
            BufferedReader reader = new BufferedReader(new FileReader(databaseFilepath));
            database = gson.fromJson(reader, objectType);
            reader.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Unable to find database file.");
        } catch (IOException e) {
            throw new IOException("Error reading from database file.");
        }

        // Ensure non-empty object is returned
        if (database == null) {
            return defaultObject;
        } else {
            return database;
        }
    }

    public void saveDatabase(T dataObject) throws IOException {
        Gson gson = new Gson();
        // Write the data object to the JSON file
        FileWriter writer;
        writer = new FileWriter(databaseFilepath);
        gson.toJson(dataObject, writer);
        writer.close();
    }
}
