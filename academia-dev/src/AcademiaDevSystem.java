import java.util.*;
import java.util.stream.Collectors;

public class AcademiaDevSystem {
    private Map<String, Course> courses = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private List<Enrollment> enrollments = new ArrayList<>();
    private Queue<SupportTicket> supportTickets = new LinkedList<>();
    
    public void initializeData() {
      
        courses.put("Java Básico", new Course("Java Básico", "Introdução ao Java", 
                "Prof. Silva", 40, DifficultyLevel.BEGINNER, CourseStatus.ACTIVE));
        courses.put("Spring Boot", new Course("Spring Boot", "Framework Spring Boot", 
                "Prof. Santos", 60, DifficultyLevel.INTERMEDIATE, CourseStatus.ACTIVE));
        courses.put("Microservices", new Course("Microservices", "Arquitetura de Microservices", 
                "Prof. Lima", 80, DifficultyLevel.ADVANCED, CourseStatus.ACTIVE));
        courses.put("Python Básico", new Course("Python Básico", "Introdução ao Python", 
                "Prof. Costa", 35, DifficultyLevel.BEGINNER, CourseStatus.INACTIVE));
        
        // Initialize users
        users.put("admin@academia.com", new Admin("Administrador", "admin@academia.com"));
        users.put("joao@email.com", new Student("João Silva", "joao@email.com", new BasicPlan()));
        users.put("maria@email.com", new Student("Maria Santos", "maria@email.com", new PremiumPlan()));
        users.put("pedro@email.com", new Student("Pedro Costa", "pedro@email.com", new BasicPlan()));
        
        System.out.println("Dados iniciais carregados com sucesso!");
    }
    
    public User authenticateUser(String email) {
        return users.get(email);
    }
    
    public void showActiveCourses() {
        System.out.println("\n=== Cursos Ativos ===");
        courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .forEach(System.out::println);
    }
    
    public void createSupportTicket(User user, String title, String message) {
        supportTickets.offer(new SupportTicket(user, title, message));
    }
    
    public void enrollStudent(Student student, String courseTitle) throws EnrollmentException {
        Course course = courses.get(courseTitle);
        if (course == null) {
            throw new EnrollmentException("Curso não encontrado");
        }
        
        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new EnrollmentException("Curso não está ativo");
        }
        
        boolean alreadyEnrolled = enrollments.stream()
                .anyMatch(e -> e.getStudent().equals(student) && e.getCourse().equals(course));
        
        if (alreadyEnrolled) {
            throw new EnrollmentException("Aluno já está matriculado neste curso");
        }
        
        long currentEnrollments = enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .count();
        
        if (!student.getSubscriptionPlan().canEnroll((int) currentEnrollments)) {
            throw new EnrollmentException("Limite de matrículas atingido para seu plano");
        }
        
        enrollments.add(new Enrollment(student, course));
    }
    
    public void showStudentEnrollments(Student student) {
        System.out.println("\n=== Minhas Matrículas ===");
        enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .forEach(System.out::println);
    }
    
    public void updateProgress(Student student, String courseTitle, int progress) throws Exception {
        Course course = courses.get(courseTitle);
        if (course == null) {
            throw new Exception("Curso não encontrado");
        }
        
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                .findFirst()
                .orElseThrow(() -> new Exception("Matrícula não encontrada"));
        
        enrollment.setProgress(progress);
    }
    
    public void cancelEnrollment(Student student, String courseTitle) throws Exception {
        Course course = courses.get(courseTitle);
        if (course == null) {
            throw new Exception("Curso não encontrado");
        }
        
        boolean removed = enrollments.removeIf(e -> 
                e.getStudent().equals(student) && e.getCourse().equals(course));
        
        if (!removed) {
            throw new Exception("Matrícula não encontrada");
        }
    }
    
    public void updateCourseStatus(String courseTitle, CourseStatus status) throws Exception {
        Course course = courses.get(courseTitle);
        if (course == null) {
            throw new Exception("Curso não encontrado");
        }
        course.setStatus(status);
    }
    
    public void updateStudentPlan(String email, String planType) throws Exception {
        User user = users.get(email);
        if (!(user instanceof Student)) {
            throw new Exception("Usuário não é um aluno");
        }
        
        Student student = (Student) user;
        SubscriptionPlan newPlan = planType.equalsIgnoreCase("PREMIUM") ? 
                new PremiumPlan() : new BasicPlan();
        student.setSubscriptionPlan(newPlan);
    }
    
    public void processNextSupportTicket() {
        SupportTicket ticket = supportTickets.poll();
        if (ticket == null) {
            System.out.println("Nenhum ticket na fila");
        } else {
            System.out.println("Processando ticket: " + ticket);
        }
    }
    
    public void getCoursesByDifficulty(DifficultyLevel level) {
        System.out.println("\n=== Cursos por Nível: " + level + " ===");
        courses.values().stream()
                .filter(course -> course.getDifficultyLevel() == level)
                .sorted(Comparator.comparing(Course::getTitle))
                .forEach(System.out::println);
    }
    
    public void getUniqueInstructors() {
        System.out.println("\n=== Instrutores Únicos (Cursos Ativos) ===");
        Set<String> instructors = courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .map(Course::getInstructorName)
                .collect(Collectors.toSet());
        instructors.forEach(System.out::println);
    }
    
    public void getStudentsByPlan() {
        System.out.println("\n=== Alunos por Plano ===");
        Map<String, List<Student>> studentsByPlan = users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.groupingBy(s -> s.getSubscriptionPlan().getPlanName()));
        
        studentsByPlan.forEach((plan, students) -> {
            System.out.println(plan + ":");
            students.forEach(s -> System.out.println("  - " + s.getName()));
        });
    }
    
    public void getAverageProgress() {
        double average = enrollments.stream()
                .mapToInt(Enrollment::getProgress)
                .average()
                .orElse(0.0);
        System.out.println("\n=== Média Geral de Progresso ===");
        System.out.printf("%.2f%%\n", average);
    }
    
    public void getStudentWithMostEnrollments() {
        Optional<Student> studentWithMost = enrollments.stream()
                .collect(Collectors.groupingBy(Enrollment::getStudent, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        
        System.out.println("\n=== Aluno com Mais Matrículas ===");
        if (studentWithMost.isPresent()) {
            Student student = studentWithMost.get();
            long count = enrollments.stream()
                    .filter(e -> e.getStudent().equals(student))
                    .count();
            System.out.println(student.getName() + " - " + count + " matrículas");
        } else {
            System.out.println("Nenhum aluno encontrado");
        }
    }
    
    public void exportCoursesToCsv() {
        List<Course> courseList = new ArrayList<>(courses.values());
        String csv = GenericCsvExporter.exportToCsv(courseList, "title", "instructorName", "durationInHours");
        System.out.println("\n=== Exportação CSV ===");
        System.out.println(csv);
    }
}
