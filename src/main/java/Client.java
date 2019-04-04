// COMP335 Assignment 1: Stage 1
// 44849516 Cameron Mills and Victor

//input libraries
import java.io.*;
import java.net.*;

public class Client {

	public static final int OK_LENGTH = 2;
	public static final int JOBN_LENGTH = 22;
	// initialize the socket and the input and output
	Socket socket = null;
	DataOutputStream output = null;
	DataInputStream input = null;
	String Hello = "HELO";
	String Auth = "AUTH comp335";
	String Ready = "REDY";
	String ioline = "Running";

	// Part 1 - sending hello
	char[] array = new char[2];
	String noob = "Kappa";
	String verify = "OK";
	int i = 0;
	Boolean confirm1 = false;

	// Part 2 - sending authentication
	char[] array1 = new char[2];
	String noob1 = "Kappa";
	String verify1 = "OK";
	int j = 0;
	Boolean cofirm2 = false;

	// part 3 - scheduling job
	String[] schedulerarray = {"SCHD", "0", "large", "0", "\n"};
	String scheduler = "SCHD 0 large 0\n";
	String givehome = "";
	char[] array2 = new char[40];
	int a = 0;
	String noob2 = "kappa";


	// Establish connection
	// establish address and port numbers
	public static void main(String args[]) {
		Client clnt = new Client();
		if (args.length != 2) {
			System.out.println("Usage: Client <server address> <server port>");
			System.exit(0);
//            Client socket = new Client("127.0.0.1", 8096);
		}
		clnt.startClient(args[0], Integer.valueOf(args[1]));
	}

	// initalize connection with server
	public void startClient(String address, int port) {
		StringBuilder inputLine = new StringBuilder();
		try {
			// connect to server
			socket = new Socket(address, port);
			System.out.println("Connected");

			// initalize input and out data stream
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());

		} catch (UnknownHostException u) {
			System.out.println(u);

		} catch (IOException i) {
			System.out.println(i);
		}

		// send messages to server

		if (!ioline.equals("QUIT")) {
			try {

				// Part 1 Hello
				// read in a byte. convert byte to char. add to char array. convert array to string

				//send HELO\n to server
				output.write(Hello.getBytes());

				//while client has not receive all data from server
				int rd = 0;

				String noob = readResponse(OK_LENGTH);
//                while (i < array.length) {
//                    // read in a byte and assigns it to an int
//                    int bs = input.read();
//                    // converts byte to char
//                    char c = (char) bs;
//                    // saving char into current array index 0[O] 1{K] 2[\n]
//                    array[i] = c;
//                    // move index up 1
//                    i++;
//
//                }

				// convert array into string turns array into OK\n
//                String noob = new String(array);
				// print out string
				System.out.println(noob);
				// if string = expected output
				if (noob.equals(verify)) {
					confirm1 = true;
				} else {
					System.out.println("error" + noob);
					System.out.println("it broke");
					closeConnection();
				}


				//Part 2 Auth

				// if we received OK\n from server we proceed and send the Auth XXXX
				if(confirm1 == true) {
					output.write(Auth.getBytes());
				}

//                while (j < array1.length) {
//                    int bs1 = input.read();
//                    char c1 = (char) bs1;
//                    array1[j] = c1;
//                    j++;
//                }

				String noob1 = readResponse(OK_LENGTH);
				System.out.println(noob1);
				if (noob1.equals(verify1)) {
					cofirm2 = true;
				} else {
					System.out.println("it broke worse");
					System.out.println("error" + noob1);
					closeConnection();

				}



				// Part 3 Ready for job
				// find largest server and allocated all jobs to it
				// read in jobs and find job id
				// repeat above in loop until none



				if(cofirm2 == true) {

					while(givehome != "QUIT") {
						// Send Ready
						output.write(Ready.getBytes());
						// Receive JOBN and info
						String jobn = readResponse(JOBN_LENGTH);

						while(givehome != "QUIT") {
							int bs2 = input.read();
							char c2 = (char) bs2;
							array2[a] = c2;
							a++;
							String s = String.valueOf(c2);
							System.out.print(s);
							if(s == "\n") {
								//System.out.println((array2[2]));
							}
							//	output.write(Ready.getBytes());
						}



						// perhaps read into an array?

						// Send Schedule

						// Receive OK and verify and repeat


					}
					String noob2 = new String(array2);
					//	System.out.println(Arrays.out(noob2));
					System.out.println(noob2);
				}

			} catch (IOException i) {
				System.out.println(i);
			}

		}

		// close the connection
		closeConnection();
	}

	private String readResponse(int responseLength) throws IOException {
		int rd;
		StringBuilder response = new StringBuilder();
		for (int i = 1; i <= responseLength; i++) {
			// read in a byte and assigns it to an int
			//      int bs = input.read();
			// converts byte to char
			response.append((char) input.read());
			// saving char into current array index 0[O] 1{K] 2[\n]
		}
		return response.toString();
	}

	private void closeConnection() {
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException i) {
			System.out.println(i);
		}
	}
}