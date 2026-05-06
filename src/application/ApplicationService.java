package application;

import common.FileUtil;
import java.util.ArrayList;
import java.util.List;

public class ApplicationService {

    public boolean applyForJob(String studentId, String jobId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            System.out.println("Error: Student ID cannot be empty");
            return false;
        }
        if (jobId == null || jobId.trim().isEmpty()) {
            System.out.println("Error: Job ID cannot be empty");
            return false;
        }

        List<String[]> applications = FileUtil.readCSV("applications.csv");
        
        for (String[] app : applications) {
            if (app.length > 1 && app[0].equals(studentId) && app[1].equals(jobId)) {
                System.out.println("Error: Already applied for this job");
                return false;
            }
        }

        String currentDate = java.time.LocalDate.now().toString();
        String[] newApplication = {studentId, jobId, "PENDING", currentDate};
        applications.add(newApplication);
        
        FileUtil.writeCSV("applications.csv", applications);
        return true;
    }

    public List<String[]> getApplicantsByJob(String jobId) {
        List<String[]> applicants = new ArrayList<>();
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        
        for (String[] app : applications) {
            if (app.length > 1 && app[1].equals(jobId)) {
                applicants.add(app);
            }
        }
        
        return applicants;
    }

    public boolean approveApplication(String studentId, String jobId, boolean approved) {
        List<String[]> applications = FileUtil.readCSV("applications.csv");
        boolean found = false;
        
        for (int i = 0; i < applications.size(); i++) {
            String[] app = applications.get(i);
            if (app.length > 1 && app[0].equals(studentId) && app[1].equals(jobId)) {
                String status = approved ? "ACCEPTED" : "REJECTED";
                String decisionDate = java.time.LocalDate.now().toString();
                
                String[] updatedApp = new String[5];
                updatedApp[0] = app[0]; // studentId
                updatedApp[1] = app[1]; // jobId
                updatedApp[2] = status;
                updatedApp[3] = app.length > 3 ? app[3] : ""; // applyDate
                updatedApp[4] = decisionDate;
                
                applications.set(i, updatedApp);
                found = true;
                break;
            }
        }
        
        if (found) {
            FileUtil.writeCSV("applications.csv", applications);
            return true;
        }
        return false;
    }
}