package academiadev.util;

import academiadev.model.*;
import java.util.*;

public class InitialData {
    
    public static Map<String, Course> createInitialCourses() {
        Map<String, Course> courses = new HashMap<>();
        
        courses.put("Java Básico", new Course("Java Básico", "Fundamentos da linguagem Java", "João Silva", 40, DifficultyLevel.BEGINNER));
        courses.put("Java Avançado", new Course("Java Avançado", "Conceitos avançados de Java", "Maria Santos", 60, DifficultyLevel.ADVANCED));
        courses.put("Spring Framework", new Course("Spring Framework", "Desenvolvimento com Spring", "Carlos Oliveira", 50, DifficultyLevel.INTERMEDIATE));
        courses.put("Python Básico", new Course("Python Básico", "Introdução ao Python", "Ana Costa", 35, DifficultyLevel.BEGINNER));
        courses.put("React JS", new Course("React JS", "Desenvolvimento frontend com React", "Pedro Lima", 45, DifficultyLevel.INTERMEDIATE));
        
        return courses;
    }
    
    public static Map<String, User> createInitialUsers() {
        Map<String, User> users = new HashMap<>();
        
        // Admins
        users.put("admin@academiadev.com", new Admin("Administrador", "admin@academiadev.com"));
        
        // Students
        users.put("aluno1@email.com", new Student("João Aluno", "aluno1@email.com", new BasicPlan()));
        users.put("aluno2@email.com", new Student("Maria Aluna", "aluno2@email.com", new PremiumPlan()));
        users.put("aluno3@email.com", new Student("Pedro Estudante", "aluno3@email.com", new BasicPlan()));
        
        return users;
    }
    
    public static List<Enrollment> createInitialEnrollments(Map<String, User> users, Map<String, Course> courses) {
        List<Enrollment> enrollments = new ArrayList<>();
        
        Student aluno1 = (Student) users.get("aluno1@email.com");
        Student aluno2 = (Student) users.get("aluno2@email.com");
        
        // Matrículas iniciais
        Enrollment e1 = new Enrollment(aluno1, courses.get("Java Básico"));
        e1.setProgress(25);
        enrollments.add(e1);
        
        Enrollment e2 = new Enrollment(aluno1, courses.get("Python Básico"));
        e2.setProgress(50);
        enrollments.add(e2);
        
        Enrollment e3 = new Enrollment(aluno2, courses.get("Java Avançado"));
        e3.setProgress(75);
        enrollments.add(e3);
        
        return enrollments;
    }
    
    public static Queue<SupportTicket> createInitialTickets(Map<String, User> users) {
        Queue<SupportTicket> tickets = new LinkedList<>();
        
        tickets.add(new SupportTicket("Problema de acesso", "Não consigo acessar o curso Java Básico", users.get("aluno1@email.com")));
        tickets.add(new SupportTicket("Dúvida sobre conteúdo", "Preciso de ajuda com exercícios", users.get("aluno2@email.com")));
        
        return tickets;
    }
}
