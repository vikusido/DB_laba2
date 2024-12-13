import java.io.*;
import java.util.*;

public class Database {
    private final String filename;
    private List<Record> records;
    private Map<Integer, Record> idMap;

    public Database(String filename) {
        this.filename = filename;
        this.records = new ArrayList<>();
        this.idMap = new HashMap<>();
    }

    public void load() throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            return; // Handle the case of a new, empty file
        }
        records.clear(); //Clear the existing records before loading
        idMap.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();
            //Skip header line if present (check for header line)
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { //Check if the line has the correct number of fields
                    try {
                        Record record = new Record(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
                        records.add(record);
                        idMap.put(record.id, record);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }
        }
    }

    public void save() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            // Write a header line (optional)
            bw.write("id,name,company,email\n");
            Collections.sort(records, Comparator.comparingInt(r -> r.id));
            for (Record record : records) {
                bw.write(record.toString());
                bw.newLine();
            }
        }
    }

    public boolean addRecord(Record record) {
        if (idMap.containsKey(record.id)) {
            return false;
        }
        records.add(record);
        idMap.put(record.id, record);
        return true;
    }

    public boolean deleteRecordById(int id) {
        if (!idMap.containsKey(id)) {
            return false;
        }
        Record recordToRemove = idMap.get(id);
        records.remove(recordToRemove);
        idMap.remove(id);
        return true;
    }

    public int deleteRecordByValue(String value, String fieldName) {
        int deletedCount = 0;
        Iterator<Record> iterator = records.iterator();
        while (iterator.hasNext()) {
            Record record = iterator.next();
            switch (fieldName) {
                case "name":
                    if (record.name.equals(value)) {
                        iterator.remove();
                        idMap.remove(record.id);
                        deletedCount++;
                    }
                    break;
                case "company":
                    if (record.company.equals(value)) {
                        iterator.remove();
                        idMap.remove(record.id);
                        deletedCount++;
                    }
                    break;
                case "email":
                    if (record.email.equals(value)) {
                        iterator.remove();
                        idMap.remove(record.id);
                        deletedCount++;
                    }
                    break;
                default:
                    System.out.println("Invalid field name: " + fieldName);
                    break;
            }
        }
        return deletedCount;
    }

    public List<Record> getRecords() {
        return records;
    }

    public Record getRecordById(int id) {
        return idMap.get(id);
    }

    public List<Record> searchByValue(String fieldName, String value) {
        List<Record> results = new ArrayList<>();
        if (!Arrays.asList("id", "name", "company", "email").contains(fieldName.toLowerCase())) {
            System.out.println("Invalid field name: " + fieldName);
            return results;
        }

        if (fieldName.equalsIgnoreCase("id")) {
            Record record = idMap.get(Integer.parseInt(value));
            if (record != null) {
                results.add(record);
            }
        } else {
            for (Record record : records) {
                switch (fieldName.toLowerCase()) {
                    case "name":
                        if (record.name.equalsIgnoreCase(value)) {
                            results.add(record);
                        }
                        break;
                    case "company":
                        if (record.company.equalsIgnoreCase(value)) {
                            results.add(record);
                        }
                        break;
                    case "email":
                        if (record.email.equalsIgnoreCase(value)) {
                            results.add(record);
                        }
                        break;
                    default:
                        System.out.println("Invalid field name: " + fieldName);
                        break;
                }
            }
        }
        return results;
    }

    public void clear() {
        records.clear();
        idMap.clear();
    }

    public void backup(String backupFilename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(backupFilename))) {
            oos.writeObject(records);
            oos.writeObject(idMap);
        }
    }

    public void restore(String backupFilename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(backupFilename))) {
            records = (List) ois.readObject();
            idMap = (Map) ois.readObject();
        }
    }

    public void importCSV(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();
            //Skip header line if present (check for header line)
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { //Check if the line has the correct number of fields
                    try {
                        Record record = new Record(Integer.parseInt(parts[0]), parts[1], parts[2], parts[3]);
                        addRecord(record);
                        idMap.put(record.id, record);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing line: " + line);
                    }
                }
            }
        }
    }

    public boolean editRecordByKey(int id, Record updatedRecord) {
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (record.getId() == id) {
                records.set(i, updatedRecord);
                idMap.put(id, updatedRecord);
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        Database db = new Database("data.csv");
        db.load(); // Load existing data if the file exists

        db.addRecord(new Record(1, "Petya", "Google", "pet@gamil.com"));
        db.addRecord(new Record(2, "Vasya", "Porsche", "vasek@yer.com"));

        db.save(); // Save changes to the file

        db.load(); //Reload to demonstrate persistence
        for (Record r : db.getRecords()) {
            System.out.println(r);
        }
    }
}