package main.domain;

public abstract class IdentificationEntity {
    private int id;

    protected IdentificationEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
