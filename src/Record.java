import java.io.Serializable; //can be saved into a byte stream or text to work with over a network

class Record implements Serializable {
    int id;
    String name;
    String company;  //Example data fields - replace with your own
    String email;

    public Record(int id, String name, String company, String email) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + company + "," + email;
    }
}