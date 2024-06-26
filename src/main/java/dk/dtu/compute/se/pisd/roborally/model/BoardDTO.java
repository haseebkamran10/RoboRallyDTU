package dk.dtu.compute.se.pisd.roborally.model;

import java.util.List;

public class BoardDTO {
    private Long id;
    private String name;
    private int width; // Changed from rows to width
    private int height; // Changed from columns to height
    private List<List<SpaceDTO>> spaces; // New field for spaces

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<List<SpaceDTO>> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<List<SpaceDTO>> spaces) {
        this.spaces = spaces;
    }
}
