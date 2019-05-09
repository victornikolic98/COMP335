package client;

// COMP335 Assignment 1: Stage 2
// 45158290 Victor Nikolic

//Import Java libraries

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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

    private static final String HELP = "-h";
    private static final String ALGORITHM = "-a";
    private static final String VERBOSE = "-v";
    private static final String ALGORITHM_CHOICE = "ff";
    public static final Pattern VALID_FIRST_CHARACTERS = Pattern.compile("^[\\\\ \\' \\-].*");


    // Initialize the socket and the input and output
    private Socket socket;
    private DataOutputStream output;
    private DataInputStream input;
    private final boolean isVerbose;
    private final boolean isFirstFit;


    public Client(boolean isVerbose, boolean isFirstFit) {
        this.isVerbose = isVerbose;
        this.isFirstFit = isFirstFit;
    }

    // Entry point
    public static void main(String[] args) {
        boolean isVerbose = false;
        boolean isFirstFit = false;

        for(int i = 0; i < args.length; i++) {
            if(HELP.equals(args[i])) {
                System.out.println("Usage: java -jar client.jar [-h] [-v] [-a algo_name]");
//                System.out.println("The only algorithm available is First-Fit (ff)");
                System.exit(0);
            } else if(VERBOSE.equals(args[i])) {
                isVerbose = true;
            } else if(ALGORITHM.equals(args[i])) {
                if(!ALGORITHM_CHOICE.equals(args[i+1])) {
                    System.out.println("Err: invalid algorithm (" + args[i+1] + ")");
                    System.out.println("Usage: java -jar client.jar [-h] [-v] [-a algo_name]");
                    System.out.println("The only algorithm available is First-Fit (ff)");
                    System.exit(0);
                }
                isFirstFit = true;
            }
//            else {
//                System.out.println("Unknown option '" + args[i] + "'");
//                System.out.println("Usage: java -jar client.jar [-h] [-v] [-a algo_name]");
//                System.out.println("The only algorithm available is First-Fit (ff)");
//                System.exit(0);
//            }
        }

        Client clnt = new Client(isVerbose, isFirstFit);
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

        Job currentJob;
        Servers availableServers = new Servers();

        while (!QUIT.equals(resp)) {

            resp = sendCommand(READY);

            if (NONE.equals(resp)) {
                resp = sendCommand(QUIT);

            } else {
                // Response is JOBN
                currentJob = new Job(resp);
                String[] tokens = resp.split(" ");
                String cmdRscd = String.format(RESC, tokens[4], tokens[5], tokens[6]);

                resp = sendCommand(cmdRscd);

                if (DATA.equals(resp)) {
                    while (!DOT_RESPONSE.equals(resp)) {
                        resp = sendCommand(OK);
                        if(!DOT_RESPONSE.equals(resp)) {
                            availableServers.add(Server.createFromResponse(resp));
                        }
                    }
                }

                Server server;
                if(isFirstFit) {
                    server = availableServers.findFirst(currentJob);
                } else {
                    server = availableServers.findLargest();
                }

                if (DOT_RESPONSE.equals(resp) && server != null) {
//                    resp = sendCommand(((String.format(SCHD, tokens[2], largest.getType(), "0"))));
                    resp = sendCommand(((String.format(SCHD, currentJob.getJobID(), server.getType(),
                            server.getId()))));
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


        if(isVerbose) {
            System.out.println("SENT: " + command);
        }

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
//            if(isVerbose)
//                System.out.println("RECEIVED: Empty response from server!");
            resp = readResponse();
        }

        if(isVerbose) {
            System.out.println("RECEIVED: " + resp);
        }

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