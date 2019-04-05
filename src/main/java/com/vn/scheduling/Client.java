package com.vn.scheduling;

// COMP335 Assignment 1: Stage 1
// 44849516 Cameron Mills and Victor Nikolic

//Import Java libraries
import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Client {

    // Server details
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8096;
    private static final int SERVER_WAIT_MILLIS = 10;

    // Common
    private static final String OK = "OK";
    private static final String QUIT = "QUIT";

    // Client commands
    private static final String HELLO = "HELO";
    private static final String AUTH = "AUTH comp335";
    private static final String READY = "REDY";
    private static final String RESC = "RESC Avail %s %s %s";
    private static final String SCHD = "SCHD %s %s %s";
//    private static final String LSTJ = "LSTJ %s %s";

    // Server responses
//    private static final String JOBN = "JOBN";
    private static final String NONE = "NONE";
    private static final String DATA = "DATA";
    private static final String ERR = "ERR";
    private static final String DOT_RESPONSE = ".";

    // Initialize the socket and the input and output
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;

    // Entry point
    public static void main(String[] args) {

        Client clnt = new Client();
        try {
            clnt.startClient();
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Error: " + Arrays.toString(e.getStackTrace()));
        }

    }

    // Start Client
    private void startClient() {

        String resp;

        connectToServer();

        // send messages to server

        // Part 1 HELLO
        // send HELO to server

        resp = sendCommand(HELLO);

        // if string = expected output

        if (!resp.equals(OK)) {
            closeConnection("Error: Server did not respond to HELO!");
        }

        //Part 2 AUTH

        resp = sendCommand(AUTH);

        if (!OK.equals(resp)) {
            closeConnection("Error: Failed to AUTH!");
        }

        // Read system.xml and find largest server

        SystemXMLParser systemXMLParser = new SystemXMLParser();
        Servers servers = systemXMLParser.loadServerList("system.xml");
        Server largest = servers.findLargest();

        // Part 3 - READY for job
        // Find the largest server and allocate all jobs to it
        // Read in jobs and find the job ID
        // Repeat the above in a loop until all jobs have been allocated

        while (!QUIT.equals(resp)) {

            resp = sendCommand(READY);

            if (NONE.equals(resp)) {
                resp = sendCommand(QUIT);
            } else {
            // Response is JOBN
                String[] tokens = resp.split(" ");
                String cmdRscd = String.format(RESC, tokens[4], tokens[5], tokens[6]);

                resp = sendCommand(cmdRscd);

                if (DATA.equals(resp)) {
                    while (!DOT_RESPONSE.equals(resp)) {
                        resp = sendCommand(OK);
                    }
                }

                if (DOT_RESPONSE.equals(resp)) {
                    resp = sendCommand(((String.format(SCHD, tokens[2], largest.getType(), "0"))));
                }
            }

        }

        // close the connection
        closeConnection("Client terminated!");

    }

    private void connectToServer() {
        try {
            // connect to server
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected");

            // initalize input and out data stream
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            closeConnection(e.getMessage());
        }
    }

    private String sendCommand(String command) {

        String resp = "";

        System.out.println("SENT: " + command);

        try {
            output.write(command.getBytes());
            TimeUnit.MILLISECONDS.sleep(SERVER_WAIT_MILLIS);
            resp = readResponse();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            closeConnection(e.getMessage());
        }

        // workaround for empty response from server
        while (resp.trim().isEmpty()) {
            System.out.println("RECEIVED: Empty response from server!");
            resp = readResponse();
        }

        System.out.println("RECEIVED: " + resp);

        if (ERR.equals(resp)) {
            closeConnection("Server returned ERR!");
        } else if (QUIT.equals(resp)) {
            closeConnection("Client terminated!");
        }

        return resp;

    }

    private String readResponse() {

        StringBuilder response = new StringBuilder();
        try {
            final int available = input.available();

            for (int i = 1; i <= available; i++) {
                response.append((char) input.read());
            }
        } catch (IOException e) {
            closeConnection(e.getMessage());
        }
        return response.toString();

    }

    private void closeConnection(String err) {

        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException i) {
            final String error = Arrays.toString(i.getStackTrace());
            System.out.println(error);
            err += error;
        }

        throw new RuntimeException("Client terminated: " + err);

    }
}