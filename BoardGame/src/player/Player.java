package player;

public class Player {
    private int id = 0;
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateTurn(int lastSet) {
        // TODO: Proper implementation
        System.out.println("My turn, last set is " + lastSet +", my set: ");
    }
}
