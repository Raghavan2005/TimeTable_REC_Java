package org.raghavan.rec.java.project.pages;

public class Rooms {
    private int roomId;
    private String roomName;
    private int roomCapacity;
    private boolean smartboard;
    private boolean speaker;

    public Rooms(int roomId, String roomName, int roomCapacity, boolean smartboard, boolean speaker) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomCapacity = roomCapacity;
        this.smartboard = smartboard;
        this.speaker = speaker;
    }

    // Getter methods
    public int getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public boolean isSmartboard() {
        return smartboard;
    }

    public boolean isSpeaker() {
        return speaker;
    }
}