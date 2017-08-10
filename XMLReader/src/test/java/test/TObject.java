package test;

public class TObject {
    private String name;
    private int nummer;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNummer() {
        return nummer;
    }
    public void setNummer(String nummer) {
        this.nummer = Integer.parseInt(nummer);
    }
    @Override
    public String toString() {
        return "TObject [name=" + name + ", nummer=" + nummer + "]";
    }
    
}
