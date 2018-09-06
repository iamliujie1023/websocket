package com.liuj.demo.websocket;

public class BBWebSocket {

    private static class Holder {
        private final static BBWebSocket INSTANCE = new BBWebSocket();
    }

    public static BBWebSocket getInstance() {
        return Holder.INSTANCE;
    }

    private BBWebSocket() {
    }

//    public void

}