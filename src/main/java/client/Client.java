package client;

// COMP335 Assignment 1: Stage 2
// 45158290 Victor Nikolic

//Import Java libraries

import java.util.Arrays;

public class Client {


    private static final String HELP = "-h";
    private static final String ALGORITHM = "-a";
    private static final String VERBOSE = "-v";
    private static final String ALGORITHM_CHOICE_FIRST_FIT = "ff";
    private static final String ALGORITHM_CHOICE_LEAST_TURNAROUND_TIME = "lt";

    public Client() {
    }

    // Entry point
    public static void main(String[] args) {
        boolean isVerbose = false;
        boolean isFirstFit = false;

        Scheduler clnt;

        for (int i = 0; i < args.length; i++) {
            if (HELP.equals(args[i])) {
                System.out.println("Usage: java -jar client.jar [-h] [-v] [-a algo_name]");
//                System.out.println("The only algorithm available is First-Fit (ff)");
                System.exit(0);
            } else if (VERBOSE.equals(args[i])) {
                isVerbose = true;
            } else if (ALGORITHM.equals(args[i])) {
                if (ALGORITHM_CHOICE_FIRST_FIT.equals(args[i + 1])) {
                    isFirstFit = true;
                } else if (ALGORITHM.equals(args[i])) {
                    if (ALGORITHM_CHOICE_LEAST_TURNAROUND_TIME.equals(args[i + 1])) {
                        isFirstFit = true;
                    } else {
                        System.out.println("Err: invalid algorithm (" + args[i + 1] + ")");
                        System.out.println("Usage: java -jar client.jar [-h] [-v] [-a algo_name]");
                        System.out.println("The only algorithm available is First-Fit (ff)");
                        System.exit(0);
                    }
                }
            }

            if (isFirstFit) {
                clnt = new FirstFitSchedulerImpl(isVerbose);
            } else {
                clnt = new AllToLargestSchedulerImpl(isVerbose);
            }

            try {
                clnt.startClient();
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Error: " + Arrays.toString(e.getStackTrace()));
            }

        }
    }
}