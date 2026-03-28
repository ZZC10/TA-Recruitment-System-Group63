package com.bupt.ta.util;

import com.bupt.ta.model.Application;
import com.bupt.ta.model.User;
import com.bupt.ta.model.Job;
import com.bupt.ta.model.MoModule;
import com.bupt.ta.model.TaProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for JSON operations using Jackson.
 * Data is stored in the 'data' directory in the project root.
 */
public class JsonUtil {
    private static final String DATA_DIR = "data";
    private static final String USER_FILE = "users.json";
    private static final String APPLICATION_FILE = "applications.json";
    private static final String JOB_FILE = "jobs.json";
    private static final String MODULE_FILE = "modules.json";
    private static final String PROFILE_FILE = "profiles.json";
    
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        // Required for LocalDateTime support in JDK 8
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Generic method to load a list of objects from a JSON file.
     */
    public static <T> List<T> loadList(String fileName, TypeReference<List<T>> typeReference) {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File file = new File(dataDir, fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return mapper.readValue(file, typeReference);
        } catch (IOException e) {
            System.err.println("Error reading JSON file: " + file.getAbsolutePath());
            return new ArrayList<>();
        }
    }

    /**
     * Generic method to save a list of objects to a JSON file.
     */
    public static <T> void saveList(String fileName, List<T> list) {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File file = new File(dataDir, fileName);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
        } catch (IOException e) {
            System.err.println("Error writing JSON file: " + file.getAbsolutePath());
        }
    }

    // --- User Methods ---
    public static List<User> loadUsers() { return loadList(USER_FILE, new TypeReference<List<User>>() {}); }
    public static void saveUsers(List<User> users) { saveList(USER_FILE, users); }
    public static void addUser(User user) { List<User> users = loadUsers(); users.add(user); saveUsers(users); }
    public static User findByUsername(String username) {
        return loadUsers().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
    }

    // --- Job Methods ---
    public static List<Job> loadJobs() { return loadList(JOB_FILE, new TypeReference<List<Job>>() {}); }
    public static void saveJobs(List<Job> jobs) { saveList(JOB_FILE, jobs); }
    public static void addJob(Job job) { List<Job> jobs = loadJobs(); jobs.add(job); saveJobs(jobs); }
    public static List<Job> findJobsByMo(String moUsername) {
        return loadJobs().stream().filter(j -> j.getMoUsername().equals(moUsername)).collect(Collectors.toList());
    }

    // --- Application Methods ---
    public static List<Application> loadApplications() { return loadList(APPLICATION_FILE, new TypeReference<List<Application>>() {}); }
    public static void saveApplications(List<Application> apps) { saveList(APPLICATION_FILE, apps); }
    public static void addApplication(Application app) { List<Application> apps = loadApplications(); apps.add(app); saveApplications(apps); }
    public static List<Application> findAppsByJob(String jobId) {
        return loadApplications().stream().filter(a -> a.getJobId().equals(jobId)).collect(Collectors.toList());
    }

    // --- Module Methods ---
    public static List<MoModule> loadModules() { return loadList(MODULE_FILE, new TypeReference<List<MoModule>>() {}); }
    public static void addModule(MoModule module) { List<MoModule> modules = loadModules(); modules.add(module); saveList(MODULE_FILE, modules); }
    public static void updateApplicationStatus(String appId, String status, String feedback) {
        List<Application> apps = loadApplications();
        for (Application app : apps) {
            if (app.getId().equals(appId)) {
                app.setStatus(status);
                app.setMoFeedback(feedback);
                app.setUpdateTime(java.time.LocalDateTime.now());
                break;
            }
        }
        saveApplications(apps);
    }
}