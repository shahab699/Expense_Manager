package Model;

public class Data {

    private int amount;
    private String type;
    private String node;
    private String id;

    public Data(int amount, String type, String node, String id, String date) {
        this.amount = amount;
        this.type = type;
        this.node = node;
        this.id = id;
        this.data = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private String data;

    public Data(){




    }
}

