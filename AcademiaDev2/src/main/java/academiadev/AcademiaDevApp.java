package academiadev;

import academiadev.model.*;
import academiadev.exception.*;
import java.util.*;
import java.util.stream.Collectors;

public class AcademiaDevApp {
    private static AcademiaDevSystem system = new AcademiaDevSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== AcademiaDev - Plataforma de Cursos Online ===");
        
        while (true) {
            if (system.getCurrentUser() == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private static void showLoginMenu() {
        System.out.println("\n--- Login ---");
        System.out.println("Emails disponíveis:");
        System.out.println("- admin@academiadev.com (Admin)");
        System.out.println("- aluno1@email.com (Student - Basic)");
        System.out.println("- aluno2@email.com (Student - Premium)");
        
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine();
        
        if (system.login(email)) {
            User user = system.getCurrentUser();
            System.out.println("Login realizado com sucesso! Bem-vindo, " + user.getName());
        } else {
            System.out.println("Email não encontrado!");
        }
    }

    private static void showMainMenu() {
        User currentUser = system.getCurrentUser();
        System.out.println("\n--- Menu Principal ---");
        System.out.println("Usuário: " + currentUser.getName());
        
        if (currentUser instanceof Admin) {
            showAdminMenu();
        } else {
            showStudentMenu();
        }
        
        System.out.println("\n0 - Logout");
        System.out.print("Escolha uma opção: ");
        
        int choice = Integer.parseInt(scanner.nextLine());
        
        if (choice == 0) {
            system = new AcademiaDevSystem();
            System.out.println("Logout realizado!");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n--- Menu Administrador ---");
        System.out.println("1 - Gerenciar Status de Cursos");
        System.out.println("2 - Atender Tickets de Suporte");
        System.out.println("3 - Gerar Relatórios");
        System.out.println("4 - Consultar Catálogo");
        
        System.out.print("Escolha uma opção: ");
        int choice = Integer.parseInt(scanner.nextLine());
        
        switch (choice) {
            case 1:
                manageCourseStatus();
                break;
            case 2:
                processSupportTickets();
                break;
            case 3:
                generateReports();
                break;
            case 4:
                showCourseCatalog();
                break;
        }
    }

    private static void showStudentMenu() {
        System.out.println("\n--- Menu Aluno ---");
        System.out.println("1 - Matricular-se em Curso");
        System.out.println("2 - Consultar Matrículas");
        System.out.println("3 - Atualizar Progresso");
        System.out.println("4 - Consultar Catálogo");
        
        System.out.print("Escolha uma opção: ");
        int choice = Integer.parseInt(scanner.nextLine());
        
        switch (choice) {
            case 1:
                enrollInCourse();
                break;
            case 2:
                showStudentEnrollments();
                break;
            case 3:
                updateProgress();
                break;
            case 4:
                showCourseCatalog();
                break;
        }
    }

    private static void manageCourseStatus() {
        System.out.println("\n--- Gerenciar Status de Cursos ---");
        List<Course> courses = system.getActiveCourses();
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.println((i + 1) + " - " + course.getTitle() + " (" + course.getStatus() + ")");
        }
        
        System.out.print("Escolha o curso (número): ");
        int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (courseIndex >= 0 && courseIndex < courses.size()) {
            Course course = courses.get(courseIndex);
            System.out.println("1 - Ativar");
            System.out.println("2 - Desativar");
            System.out.print("Escolha a ação: ");
            int action = Integer.parseInt(scanner.nextLine());
            
            try {
                if (action == 1) {
                    system.activateCourse(course.getTitle());
                    System.out.println("Curso ativado com sucesso!");
                } else if (action == 2) {
                    system.deactivateCourse(course.getTitle());
                    System.out.println("Curso desativado com sucesso!");
                }
            } catch (EnrollmentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void processSupportTickets() {
        System.out.println("\n--- Atender Tickets de Suporte ---");
        System.out.println("Tickets na fila: " + system.getTicketQueueSize());
        
        if (system.getTicketQueueSize() > 0) {
            try {
                SupportTicket ticket = system.processNextTicket();
                System.out.println("Ticket processado:");
                System.out.println("ID: " + ticket.getTicketId());
                System.out.println("Título: " + ticket.getTitle());
                System.out.println("Usuário: " + ticket.getUser().getName());
            } catch (EnrollmentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } else {
            System.out.println("Nenhum ticket na fila.");
        }
    }

    private static void generateReports() {
        System.out.println("\n--- Relatórios e Análises ---");
        
        // Cursos por dificuldade
        System.out.println("\n1. Cursos por dificuldade:");
        for (DifficultyLevel level : DifficultyLevel.values()) {
            List<Course> courses = system.getCoursesByDifficulty(level);
            System.out.println(level + ": " + courses.size() + " cursos");
        }
        
        // Instrutores únicos
        System.out.println("\n2. Instrutores únicos:");
        Set<String> instructors = system.getUniqueInstructors();
        instructors.forEach(i -> System.out.println("  - " + i));
        
        // Média de progresso
        System.out.println("\n3. Média geral de progresso: " + 
                          String.format("%.2f", system.getAverageProgress()) + "%");
    }

    private static void showCourseCatalog() {
        System.out.println("\n--- Catálogo de Cursos ---");
        List<Course> courses = system.getActiveCourses();
        if (courses.isEmpty()) {
            System.out.println("Nenhum curso ativo disponível.");
        } else {
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                System.out.println((i + 1) + " - " + course.getTitle());
                System.out.println("    Instrutor: " + course.getInstructorName());
                System.out.println("    Duração: " + course.getDurationInHours() + "h");
                System.out.println("    Dificuldade: " + course.getDifficultyLevel());
                System.out.println();
            }
        }
    }

    private static void enrollInCourse() {
        System.out.println("\n--- Matricular-se em Curso ---");
        showCourseCatalog();
        
        List<Course> courses = system.getActiveCourses();
        if (!courses.isEmpty()) {
            System.out.print("Escolha o curso (número): ");
            int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;
            
            if (courseIndex >= 0 && courseIndex < courses.size()) {
                Course course = courses.get(courseIndex);
                try {
                    system.enrollInCourse(course.getTitle());
                    System.out.println("Matrícula realizada com sucesso!");
                } catch (EnrollmentException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        }
    }

    private static void showStudentEnrollments() {
        System.out.println("\n--- Minhas Matrículas ---");
        List<Enrollment> enrollments = system.getStudentEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("Você não está matriculado em nenhum curso.");
        } else {
            for (Enrollment enrollment : enrollments) {
                System.out.println("Curso: " + enrollment.getCourse().getTitle());
                System.out.println("Progresso: " + enrollment.getProgress() + "%");
                System.out.println();
            }
        }
    }

    private static void updateProgress() {
        System.out.println("\n--- Atualizar Progresso ---");
        List<Enrollment> enrollments = system.getStudentEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("Você não está matriculado em nenhum curso.");
            return;
        }
        
        for (int i = 0; i < enrollments.size(); i++) {
            Enrollment enrollment = enrollments.get(i);
            System.out.println((i + 1) + " - " + enrollment.getCourse().getTitle() + 
                              " (Progresso atual: " + enrollment.getProgress() + "%)");
        }
        
        System.out.print("Escolha o curso (número): ");
        int courseIndex = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (courseIndex >= 0 && courseIndex < enrollments.size()) {
            System.out.print("Novo progresso (0-100): ");
            int progress = Integer.parseInt(scanner.nextLine());
            
            try {
                system.updateProgress(enrollments.get(courseIndex).getCourse().getTitle(), progress);
                System.out.println("Progresso atualizado com sucesso!");
            } catch (EnrollmentException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }
}
