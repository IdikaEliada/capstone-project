package service;

import interfaces.Persistable;
import interfaces.Searchable;
import model.Result;
import model.Student;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * StudentService - shared by both UI versions.
 * - stores students in ArrayList + HashMap
 * - provides sorting (quicksort, bubble), searching (linear, binary)
 * - persistence (serialization)
 */
public class StudentService implements Persistable, Searchable<Student>, Serializable {
    private final List<Student> students = new ArrayList<>();
    private final Map<String, Student> byId = new HashMap<>();

    public boolean addStudent(Student s) {
        if (s == null) return false;
        String id = s.getId();
        if (id == null || id.trim().isEmpty()) return false;
        if (byId.containsKey(id.toLowerCase())) {
            JOptionPane.showMessageDialog(null, "Duplicate ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        students.add(s);
        byId.put(id.toLowerCase(), s);
        return true;
    }

    public List<Student> getStudents() { return new ArrayList<>(students); }

    public Student findById(String id) {
        if (id == null) return null;
        return byId.get(id.toLowerCase());
    }

    // Binary search by id (assumes sorted by id)
    public Student binarySearchById(String id) {
        if (id == null) return null;
        sortById();
        int lo = 0, hi = students.size()-1;
        String key = id.toLowerCase();
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            String midId = students.get(mid).getId().toLowerCase();
            int cmp = midId.compareTo(key);
            if (cmp == 0) return students.get(mid);
            if (cmp < 0) lo = mid + 1; else hi = mid - 1;
        }
        return null;
    }

    public List<Student> linearSearchByName(String token) {
        List<Student> out = new ArrayList<>();
        if (token == null || token.trim().isEmpty()) return out;
        String t = token.toLowerCase();
        for (Student s : students) {
            if (s.getFirstName().toLowerCase().contains(t) || s.getLastName().toLowerCase().contains(t)) out.add(s);
        }
        return out;
    }

    // Sorting
    public void quickSortByName() { quickSort(students, 0, students.size()-1); }
    private void quickSort(List<Student> list, int low, int high) {
        if (low < high) {
            int p = partition(list, low, high);
            quickSort(list, low, p-1);
            quickSort(list, p+1, high);
        }
    }
    private int partition(List<Student> list, int low, int high) {
        Student pivot = list.get(high);
        int i = low - 1;
        for (int j=low;j<high;j++) {
            if (list.get(j).compareTo(pivot) <= 0) {
                i++; Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i+1, high);
        return i+1;
    }

    public void bubbleSortByCgpaDesc() {
        int n = students.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i=1;i<n;i++) {
                if (students.get(i-1).getComputedCgpa() < students.get(i).getComputedCgpa()) {
                    Collections.swap(students, i-1, i);
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
    }

    public void sortById() { students.sort(Comparator.comparing(s -> s.getId().toLowerCase())); }

    // Summaries
    public double classAverageScore() {
        if (students.isEmpty()) return 0.0;
        double sum = 0;
        for (Student s : students) sum += s.getComputedCgpa();
        return sum / students.size();
    }

    public Student topPerformer() {
        return students.stream().max(Comparator.comparingDouble(Student::getComputedCgpa)).orElse(null);
    }

    // Persistence
    @Override
    public void saveToFile(String filePath) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    @Override
    public void loadFromFile(String filePath) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            StudentService loaded = (StudentService) ois.readObject();
            this.students.clear();
            this.students.addAll(loaded.students);
            this.byId.clear();
            for (Student s : students) byId.put(s.getId().toLowerCase(), s);
        }
    }
}