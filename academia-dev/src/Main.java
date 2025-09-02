import java.util.Scanner;

public class Main {
    private static AcademiaDevSystem system = new AcademiaDevSystem();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        system.initializeData();
        
        System.out.println("=== Bem-vindo à AcademiaDev ===");
        
        while (true) {
            if (currentUser == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }
    
    private static void showLoginMenu() {
        System.out.println("\n=== SISTEMA ACADEMIADEV ===");
        System.out.println("1. Login");
        System.out.println("2. Ver usuários disponíveis");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                showAvailableUsers();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Opção inválida!");
        }
    }
    
    private static void showAvailableUsers() {
        System.out.println("\n=== USUÁRIOS DISPONÍVEIS PARA LOGIN ===");
        System.out.println("ADMINISTRADOR:");
        System.out.println("  • admin@academia.com");
        System.out.println("\nESTUDANTES:");
        System.out.println("  • joao@email.com (Plano Básico)");
        System.out.println("  • maria@email.com (Plano Premium)");
        System.out.println("  • pedro@email.com (Plano Básico)");
        System.out.println("\nDigite exatamente um dos emails acima para fazer login.");
    }
    
    private static void login() {
        System.out.println("\n=== LOGIN ===");
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine().trim();
        
        if (email.isEmpty()) {
            System.out.println("❌ Email não pode estar vazio!");
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("❌ Formato de email inválido!");
            return;
        }
        
        currentUser = system.authenticateUser(email);
        if (currentUser == null) {
            System.out.println("❌ Usuário não encontrado!");
            System.out.println("💡 Dica: Digite '2' no menu para ver os usuários disponíveis.");
        } else {
            System.out.println("✅ Login realizado com sucesso!");
            System.out.println("👋 Bem-vindo, " + currentUser.getName());
            
            if (currentUser instanceof Student) {
                Student student = (Student) currentUser;
                System.out.println("📋 Plano: " + student.getSubscriptionPlan().getPlanName());
            } else if (currentUser instanceof Admin) {
                System.out.println("🔧 Acesso de Administrador");
            }
        }
    }
    
    private static void showMainMenu() {
        System.out.println("\n=== Menu Principal ===");
        System.out.println("1. Consultar Catálogo de Cursos");
        System.out.println("2. Abrir Ticket de Suporte");
        
        if (currentUser instanceof Admin) {
            showAdminMenu();
        } else if (currentUser instanceof Student) {
            showStudentMenu();
        }
        
        System.out.println("0. Logout");
        System.out.print("Escolha uma opção: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        handleMenuChoice(choice);
    }
    
    private static void showAdminMenu() {
        System.out.println("3. Gerenciar Status de Cursos");
        System.out.println("4. Gerenciar Planos de Alunos");
        System.out.println("5. Atender Tickets de Suporte");
        System.out.println("6. Gerar Relatórios");
        System.out.println("7. Exportar Dados");
    }
    
    private static void showStudentMenu() {
        System.out.println("3. Matricular-se em Curso");
        System.out.println("4. Consultar Minhas Matrículas");
        System.out.println("5. Atualizar Progresso");
        System.out.println("6. Cancelar Matrícula");
    }
    
    private static void handleMenuChoice(int choice) {
        try {
            switch (choice) {
                case 0:
                    currentUser = null;
                    System.out.println("Logout realizado com sucesso!");
                    break;
                case 1:
                    system.showActiveCourses();
                    break;
                case 2:
                    openSupportTicket();
                    break;
                case 3:
                    if (currentUser instanceof Admin) {
                        manageCourseStatus();
                    } else {
                        enrollInCourse();
                    }
                    break;
                case 4:
                    if (currentUser instanceof Admin) {
                        manageStudentPlans();
                    } else {
                        showMyEnrollments();
                    }
                    break;
                case 5:
                    if (currentUser instanceof Admin) {
                        processSupportTickets();
                    } else {
                        updateProgress();
                    }
                    break;
                case 6:
                    if (currentUser instanceof Admin) {
                        generateReports();
                    } else {
                        cancelEnrollment();
                    }
                    break;
                case 7:
                    if (currentUser instanceof Admin) {
                        exportData();
                    }
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void openSupportTicket() {
        System.out.print("Título do ticket: ");
        String title = scanner.nextLine();
        System.out.print("Mensagem: ");
        String message = scanner.nextLine();
        
        system.createSupportTicket(currentUser, title, message);
        System.out.println("Ticket criado com sucesso!");
    }
    
    private static void enrollInCourse() {
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine();
        
        try {
            system.enrollStudent((Student) currentUser, courseTitle);
            System.out.println("Matrícula realizada com sucesso!");
        } catch (EnrollmentException e) {
            System.out.println("Erro na matrícula: " + e.getMessage());
        }
    }
    
    private static void showMyEnrollments() {
        system.showStudentEnrollments((Student) currentUser);
    }
    
    private static void updateProgress() {
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine();
        System.out.print("Digite o novo progresso (0-100): ");
        int progress = scanner.nextInt();
        scanner.nextLine();
        
        try {
            system.updateProgress((Student) currentUser, courseTitle, progress);
            System.out.println("Progresso atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void cancelEnrollment() {
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine();
        
        try {
            system.cancelEnrollment((Student) currentUser, courseTitle);
            System.out.println("Matrícula cancelada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void manageCourseStatus() {
        System.out.print("Digite o título do curso: ");
        String courseTitle = scanner.nextLine();
        System.out.print("Novo status (ACTIVE/INACTIVE): ");
        String status = scanner.nextLine();
        
        try {
            system.updateCourseStatus(courseTitle, CourseStatus.valueOf(status.toUpperCase()));
            System.out.println("Status do curso atualizado!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void manageStudentPlans() {
        System.out.print("Digite o email do aluno: ");
        String email = scanner.nextLine();
        System.out.print("Novo plano (BASIC/PREMIUM): ");
        String plan = scanner.nextLine();
        
        try {
            system.updateStudentPlan(email, plan);
            System.out.println("Plano do aluno atualizado!");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private static void processSupportTickets() {
        system.processNextSupportTicket();
    }
    
    private static void generateReports() {
        System.out.println("\n=== Relatórios ===");
        System.out.println("1. Cursos por Nível de Dificuldade");
        System.out.println("2. Instrutores Únicos");
        System.out.println("3. Alunos por Plano");
        System.out.println("4. Média de Progresso");
        System.out.println("5. Aluno com Mais Matrículas");
        System.out.print("Escolha um relatório: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                System.out.print("Digite o nível (BEGINNER/INTERMEDIATE/ADVANCED): ");
                String level = scanner.nextLine();
                system.getCoursesByDifficulty(DifficultyLevel.valueOf(level.toUpperCase()));
                break;
            case 2:
                system.getUniqueInstructors();
                break;
            case 3:
                system.getStudentsByPlan();
                break;
            case 4:
                system.getAverageProgress();
                break;
            case 5:
                system.getStudentWithMostEnrollments();
                break;
        }
    }
    
    private static void exportData() {
        System.out.println("Funcionalidade de exportação CSV implementada!");
        system.exportCoursesToCsv();
    }
}
