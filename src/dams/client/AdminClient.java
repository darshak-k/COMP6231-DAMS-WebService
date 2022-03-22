package dams.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import dams.MTLServerGenerated.MTLHospitalServerService;
import dams.QUEServerGenerated.QUEHospitalServerService;
import dams.SHEServerGenerated.SHEHospitalServerService;
import dams.model.AppointmentType;
import dams.model.Configuration;

public class AdminClient {
	private static String adminID = "";
	private static MTLHospitalServerService mtlServerService = new MTLHospitalServerService();
	private static QUEHospitalServerService queServerService = new QUEHospitalServerService();
	private static SHEHospitalServerService sheServerService = new SHEHospitalServerService();

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: <AdminClient> <adminId>");
			return;
		}

		adminID = args[0];

		if (!verifyAdminDetails(adminID)) {
			System.out.println("Not Valid Admin ID: <MTL/QUE/SHE><A><XXXX>");
			return;
		}

		try {

			System.out.println("Logged in User, " + adminID + "!");

			runAdminAllOptions();
		} catch (Exception e) {
		}
	}

	private static void runAdminAllOptions() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		String logFileName = adminID + ".log";
		String serverName = adminID.substring(0, 3);

		boolean exit = false;

		while (!exit) {
			try {
				System.out.println("===========================================");
				System.out.println("                   MENU                    ");
				System.out.println("===========================================");
				System.out.println("1. Add Appointment");
				System.out.println("2. Remove Appointment");
				System.out.println("3. List Appointment Availability");
				System.out.println("4. Book Appointment");
				System.out.println("5. Get Appointment SChedule");
				System.out.println("6. Cancel Appointment");
				System.out.println("7. Swap Appointment");
				System.out.println("8. Exit");
				System.out.println("");

				System.out.print("Enter your choice > ");
				int choice = scanner.nextInt();

				String requestDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
						.format(Calendar.getInstance().getTime());

				if (choice == 1) {
					AppointmentType appointmentType = getAppointmentType();
					String appointmentId = getAppointmentId();
					int capacity = getCapacity();

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort().addAppointment(appointmentId,
								appointmentType, capacity);

					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort().addAppointmentQUE(appointmentId,
								appointmentType, capacity);

					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort().addAppointmentSHE(appointmentId,
								appointmentType, capacity);

					}

					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(appointmentId);
					requestParameters.add(appointmentType.toString());
					requestParameters.add(capacity + "");

					boolean completed = serverResponse.contains("Succss");

					addRecordIntoLogFile(logFileName, requestDate, "Add appointment", requestParameters, completed,
							serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 2) {
					AppointmentType appointmentType = getAppointmentType();
					String appointmentId = getAppointmentId();

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort().removeAppointment(appointmentId,
								appointmentType);

					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort().removeAppointmentQUE(appointmentId,
								appointmentType);

					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort().removeAppointmentSHE(appointmentId,
								appointmentType);

					}

					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(appointmentId);
					requestParameters.add(appointmentType.toString());

					boolean completed = serverResponse.contains("Success");

					addRecordIntoLogFile(logFileName, requestDate, "Remove appointment", requestParameters, completed,
							serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 3) {
					AppointmentType appointmentType = getAppointmentType();

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort()
								.listAppointmentAvailability(appointmentType);
					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort()
								.listAppointmentAvailabilityQUE(appointmentType);
					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort()
								.listAppointmentAvailabilitySHE(appointmentType);
					}

					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(appointmentType.toString());

					boolean completed = serverResponse.contains("Success");

					addRecordIntoLogFile(logFileName, requestDate, "List appointment", requestParameters, completed,
							serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 4) {
					AppointmentType appointmentType = getAppointmentType();
					String appointmentId = getAppointmentId();

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort().bookAppointment(adminID,
								appointmentId, appointmentType);

					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort().bookAppointmentQUE(adminID,
								appointmentId, appointmentType);
					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort().bookAppointmentSHE(adminID,
								appointmentId, appointmentType);
					}

					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(adminID);
					requestParameters.add(appointmentId);
					requestParameters.add(appointmentType.toString());

					boolean completed = serverResponse.contains("Success");

					addRecordIntoLogFile(logFileName, requestDate, "Book appointment by " + adminID, requestParameters,
							completed, serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 5) {

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort().getAppointmentSchedule(adminID);
					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort().getAppointmentScheduleQUE(adminID);
					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort().getAppointmentScheduleSHE(adminID);
					}

					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(adminID);

					boolean completed = serverResponse.contains("Success");

					addRecordIntoLogFile(logFileName, requestDate, "Get Appointment Schedule", requestParameters,
							completed, serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 6) {
					String appointmentId = getAppointmentId();
					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(adminID);
					requestParameters.add(appointmentId);

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort().cancelAppointment(adminID,
								appointmentId);
					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort().cancelAppointmentQUE(adminID,
								appointmentId);
					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort().cancelAppointmentSHE(adminID,
								appointmentId);
					}

					boolean completed = serverResponse.contains("Success");

					addRecordIntoLogFile(logFileName, requestDate, "Cancel Appointment Schedule", requestParameters,
							completed, serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 7) {
					System.out.println("OLD APPOINTMENT DETAILS");
					String oldAppointmentId = getAppointmentId();
					AppointmentType oldAppointmentType = getAppointmentType();
					System.out.println("NEW APPOINTMENT ID");
					String newAppointmentId = getAppointmentId();
					AppointmentType newAppointmentType = getAppointmentType();
					List<String> requestParameters = new ArrayList<String>();
					requestParameters.add(adminID);
					requestParameters.add(oldAppointmentId);
					requestParameters.add(oldAppointmentType.toString());
					requestParameters.add(newAppointmentId);
					requestParameters.add(newAppointmentType.toString());

					String serverResponse = "";

					if (serverName.equals("MTL")) {
						serverResponse = mtlServerService.getMTLHospitalServerPort().swapAppointment(adminID,
								oldAppointmentId, oldAppointmentType, newAppointmentId, newAppointmentType);
					} else if (serverName.equals("QUE")) {
						serverResponse = queServerService.getQUEHospitalServerPort().swapAppointmentQUE(adminID,
								oldAppointmentId, oldAppointmentType, newAppointmentId, newAppointmentType);
					} else if (serverName.equals("SHE")) {
						serverResponse = sheServerService.getSHEHospitalServerPort().swapAppointmentSHE(adminID,
								oldAppointmentId, oldAppointmentType, newAppointmentId, newAppointmentType);
					}

					boolean completed = serverResponse.contains("Success");

					addRecordIntoLogFile(logFileName, requestDate, "Swap Appointment Schedule", requestParameters,
							completed, serverResponse);

					System.out.println();
					System.out.println(serverResponse);
				} else if (choice == 8) {
					exit = true;
				} else {
					System.out.println("Not valid option. Try again.");
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Not valid option. Try again.");
			}
		}
	}

	public static void addRecordIntoLogFile(String fileName, String requestDate, String requestType,
			List<String> requestParameter, boolean completed, String response) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		try {

			File f = new File(Configuration.ADMIN_LOG_FILE_PATH + fileName);
			if (!f.exists()) {
				new File(Configuration.ADMIN_LOG_FILE_PATH).mkdirs();
				f.createNewFile();
			}

			fw = new FileWriter(Configuration.ADMIN_LOG_FILE_PATH + fileName, true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			pw.println(requestDate + " | " + requestType + " | " + requestParameter.toString() + " | " + response
					+ " | " + completed);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pw.close();
				bw.close();
				fw.close();
			} catch (IOException io) {
			}
		}

	}

	private static int getCapacity() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter Capacity > ");
		int capacity = scanner.nextInt();

		return capacity;
	}

	private static String getAppointmentId() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter appointmentID [(MTL|SHE|QUE)(A|E|M)(MMDDYY)] > ");
		String appointmentId = scanner.next();

		while (!appointmentId
				.matches("(MTL|SHE|QUE)(A|E|M)(0[1-9]|1[0-9]|2[0-9]|3[01])(0[1-9]|11|12)(0[1-9]|1[0-9]|2[0-2])")) {
			System.out.print("Invalid ID Try again. Enter appointmentID [(MTL|SHE|QUE)(A|E|M)(MMDDYY)] > ");
			appointmentId = scanner.next();
		}
		return appointmentId;
	}

	private static AppointmentType getAppointmentType() {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(System.in);
		boolean exit = false;
		System.out.println("===========================================");
		System.out.println("            Appointment Type               ");
		System.out.println("===========================================");
		System.out.println("1. Physician");
		System.out.println("2. Surgeon");
		System.out.println("3. Dental");
		System.out.println("4. Exit");
		System.out.println("");

		System.out.print("Enter your choice > ");
		while (!exit) {

			int choice = scanner.nextInt();

			if (choice == 1) {
				return AppointmentType.PHYSICIAN;
			} else if (choice == 2) {
				return AppointmentType.SURGEON;
			} else if (choice == 3) {
				return AppointmentType.DENTAL;
			} else {
				System.out.println("Try again. Enter your choice > ");
			}
		}

		return null;
	}

	private static boolean verifyAdminDetails(String adminID) {
		// TODO Auto-generated method stub
		if (adminID.startsWith("MTLA") && adminID.length() == 8) {
			return true;
		}

		if (adminID.startsWith("QUEA") && adminID.length() == 8) {
			return true;
		}

		if (adminID.startsWith("SHEA") && adminID.length() == 8) {
			return true;
		}

		return false;
	}
}
