package POJO;

public class Root{
    public int id;
    public String name;
    public String username;
    public String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address address;
    public String phone;
    public String website;
    public Company company;
}