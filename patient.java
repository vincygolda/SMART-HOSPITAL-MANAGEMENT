public class Patient {
    int id;
    String name;
    int age;
    int emergencyLevel;

    public Patient(int id, String name, int age, int emergencyLevel) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.emergencyLevel = emergencyLevel;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public void displayDetails() {
        System.out.println("Patient ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);

        if (emergencyLevel == 1) {
            System.out.println("Emergency Level: 1 - Critical");
        } else if (emergencyLevel == 2) {
            System.out.println("Emergency Level: 2 - Urgent");
        } else {
            System.out.println("Emergency Level: 3 - Normal");
        }
    }
}