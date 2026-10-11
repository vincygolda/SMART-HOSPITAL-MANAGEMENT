import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.io.*;

public class Main {

    static final String FILE_NAME = "patients.txt";
    static final String HISTORY_FILE = "treatment_history.txt";

    // Estimated treatment duration for each priority level
    public static int getTreatmentMinutes(Patient p) {
        if (p.emergencyLevel == 1) {
            return 15; // Critical
        } else if (p.emergencyLevel == 2) {
            return 30; // Urgent
        } else {
            return 45; // Normal
        }
    }

    // Estimate waiting time based on patients ahead in the queue
    public static int getWaitingTime(
            Patient target, PriorityQueue<Patient> queue) {

        PriorityQueue<Patient> temp = new PriorityQueue<>(queue);
        int waitingMinutes = 0;

        while (!temp.isEmpty()) {
            Patient p = temp.poll();

            if (p.id == target.id) {
                return waitingMinutes;
            }

            waitingMinutes += getTreatmentMinutes(p);
        }

        return -1;
    }

    public static void savePatients(PriorityQueue<Patient> queue)
            throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(FILE_NAME))) {

            PriorityQueue<Patient> temp = new PriorityQueue<>(queue);

            while (!temp.isEmpty()) {
                Patient p = temp.poll();

                writer.write(p.id + "\t" + p.name.replace("\t", " ")
                        + "\t" + p.age + "\t" + p.emergencyLevel);
                writer.newLine();
            }
        }
    }

    public static int loadPatients(PriorityQueue<Patient> queue) {
        int nextId = 101;

        try (BufferedReader reader = new BufferedReader(
                new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t", -1);

                if (data.length != 4) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    int age = Integer.parseInt(data[2]);
                    int level = Integer.parseInt(data[3]);

                    if (id >= 101 && !name.isEmpty()
                            && age >= 1 && age <= 120
                            && level >= 1 && level <= 3) {

                        queue.offer(
                                new Patient(id, name, age, level));

                        nextId = Math.max(nextId, id + 1);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid patient record.");
                }
            }

        } catch (IOException e) {
            System.out.println("No saved patient records found.");
        }

        return nextId;
    }

    public static void saveTreatment(Patient p) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(HISTORY_FILE, true))) {

            writer.write(p.id + "\t" + p.name.replace("\t", " ")
                    + "\t" + p.age + "\t" + p.emergencyLevel);
            writer.newLine();
        }
    }

    public static void viewHistory() {
        System.out.println("\n--- TREATMENT HISTORY ---");

        try (BufferedReader reader = new BufferedReader(
                new FileReader(HISTORY_FILE))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t", -1);

                if (data.length == 4) {
                    System.out.println("Patient ID: " + data[0]);
                    System.out.println("Name: " + data[1]);
                    System.out.println("Age: " + data[2]);

                    int level = Integer.parseInt(data[3]);

                    if (level == 1) {
                        System.out.println("Emergency: Critical");
                    } else if (level == 2) {
                        System.out.println("Emergency: Urgent");
                    } else {
                        System.out.println("Emergency: Normal");
                    }

                    System.out.println("--------------------");
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No treatment history yet.");
            }

        } catch (IOException e) {
            System.out.println("No treatment history yet.");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        PriorityQueue<Patient> queue = new PriorityQueue<>(
                Comparator.comparingInt(Patient::getEmergencyLevel)
                        .thenComparingInt(p -> p.id));

        int nextId = loadPatients(queue);
        boolean running = true;

        System.out.println("\n==================================");
        System.out.println(" SMART HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("==================================");

        while (running) {

            System.out.println("\n1. Register Patient");
            System.out.println("2. View Waiting Queue");
            System.out.println("3. Treat Next Patient");
            System.out.println("4. View Treatment History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Enter a number from 1 to 5.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Patient Name: ");
                    String name = sc.nextLine().trim();

                    if (name.isEmpty() || name.contains("\t")) {
                        System.out.println("Invalid patient name!");
                        break;
                    }

                    System.out.print("Enter Patient Age: ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Age must be a number.");
                        sc.nextLine();
                        break;
                    }

                    int age = sc.nextInt();
                    sc.nextLine();

                    if (age < 1 || age > 120) {
                        System.out.println("Invalid age!");
                        break;
                    }

                    System.out.println("Emergency Level:");
                    System.out.println("1 - Critical");
                    System.out.println("2 - Urgent");
                    System.out.println("3 - Normal");
                    System.out.print("Enter level (1-3): ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Level must be a number.");
                        sc.nextLine();
                        break;
                    }

                    int level = sc.nextInt();
                    sc.nextLine();

                    if (level < 1 || level > 3) {
                        System.out.println("Invalid emergency level!");
                        break;
                    }

                    Patient patient = new Patient(
                            nextId, name, age, level);

                    queue.offer(patient);

                    try {
                        savePatients(queue);

                        System.out.println("Patient registered and saved!");
                        System.out.println("Patient ID: " + nextId);
                        System.out.println("Estimated waiting time: "
                                + getWaitingTime(patient, queue)
                                + " minutes");

                        nextId++;

                    } catch (IOException e) {
                        queue.remove(patient);
                        System.out.println("Error saving patient records!");
                    }
                    break;

                case 2:
                    if (queue.isEmpty()) {
                        System.out.println("No patients waiting.");
                    } else {
                        PriorityQueue<Patient> temp =
                                new PriorityQueue<>(queue);

                        System.out.println("\n--- PATIENT WAITING QUEUE ---");

                        while (!temp.isEmpty()) {
                            Patient p = temp.poll();

                            p.displayDetails();

                            System.out.println("Estimated waiting time: "
                                    + getWaitingTime(p, queue)
                                    + " minutes");

                            System.out.println("Estimated treatment duration: "
                                    + getTreatmentMinutes(p)
                                    + " minutes");

                            System.out.println("--------------------");
                        }

                        System.out.println(
                                "Note: Waiting times are estimates.");
                    }
                    break;

                case 3:
                    if (queue.isEmpty()) {
                        System.out.println("No patients to treat.");
                    } else {
                        Patient nextPatient = queue.poll();

                        try {
                            saveTreatment(nextPatient);
                            savePatients(queue);

                            System.out.println("\n--- NEXT PATIENT ---");
                            nextPatient.displayDetails();
                            System.out.println(
                                    "Treatment recorded successfully!");

                        } catch (IOException e) {
                            queue.offer(nextPatient);

                            try {
                                savePatients(queue);
                            } catch (IOException ignored) {
                                System.out.println(
                                        "Warning: Queue file could not be updated.");
                            }

                            System.out.println(
                                    "Error saving treatment history!");
                        }
                    }
                    break;

                case 4:
                    viewHistory();
                    break;

                case 5:
                    running = false;
                    System.out.println(
                            "Thank you for using Smart Hospital System!");
                    break;

                default:
                    System.out.println("Invalid choice! Enter 1 to 5.");
            }
        }

        sc.close();
    }
}