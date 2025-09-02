package academiadev;

import academiadev.model.*;
import academiadev.util.*;
import academiadev.exception.*;
import java.util.*;
import java.util.stream.Collectors;

public class AcademiaDevSystem {
    private Map<String, Course> courses;
    private Map<String, User> users;
    private List<Enrollment> enrollments;
    private Queue<SupportTicket> supportTickets;
    private User currentUser;

    public AcademiaDevSystem() {
        this.courses = InitialData.createInitialCourses();
        this.users = InitialData.createInitialUsers();
        this.enrollments = InitialData.createInitialEnrollments(users, courses);
        this.supportTickets = InitialData.createInitialTickets(users);
    }

    // Autenticação
    public boolean login(String email) {
        User user = users.get(email);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // Gerenciamento de Cursos
    public void activateCourse(String courseTitle) {
        if (!(currentUser instanceof Admin)) {
            throw new EnrollmentException("Apenas administradores podem ativar/desativar cursos");
        }
        
        Course course = courses.get(courseTitle);
        if (course != null) {
            course.setStatus(CourseStatus.ACTIVE);
        }
    }

    public void deactivateCourse(String courseTitle) {
        if (!(currentUser instanceof Admin)) {
            throw new EnrollmentException("Apenas administradores podem ativar/desativar cursos");
        }
        
        Course course = courses.get(courseTitle);
        if (course != null) {
            course.setStatus(CourseStatus.INACTIVE);
        }
    }

    public List<Course> getActiveCourses() {
        return courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    // Gerenciamento de Usuários
    public void changeStudentPlan(String studentEmail, SubscriptionPlan newPlan) {
        if (!(currentUser instanceof Admin)) {
            throw new EnrollmentException("Apenas administradores podem alterar planos");
        }
        
        User user = users.get(studentEmail);
        if (user instanceof Student) {
            ((Student) user).setSubscriptionPlan(newPlan);
        }
    }

    // Sistema de Matrículas
    public void enrollInCourse(String courseTitle) {
        if (!(currentUser instanceof Student)) {
            throw new EnrollmentException("Apenas alunos podem se matricular em cursos");
        }
        
        Student student = (Student) currentUser;
        Course course = courses.get(courseTitle);
        
        if (course == null) {
            throw new EnrollmentException("Curso não encontrado");
        }
        
        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new EnrollmentException("Curso não está ativo");
        }
        
        // Verificar se já está matriculado
        boolean alreadyEnrolled = enrollments.stream()
                .anyMatch(e -> e.getStudent().equals(student) && e.getCourse().equals(course));
        
        if (alreadyEnrolled) {
            throw new EnrollmentException("Aluno já está matriculado neste curso");
        }
        
        // Verificar limite do plano
        long currentEnrollments = enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .count();
        
        if (!student.getSubscriptionPlan().canEnroll((int) currentEnrollments)) {
            throw new EnrollmentException("Limite de matrículas atingido para o plano atual");
        }
        
        enrollments.add(new Enrollment(student, course));
    }

    public List<Enrollment> getStudentEnrollments() {
        if (!(currentUser instanceof Student)) {
            return new ArrayList<>();
        }
        
        Student student = (Student) currentUser;
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .collect(Collectors.toList());
    }

    public void updateProgress(String courseTitle, int progress) {
        if (!(currentUser instanceof Student)) {
            throw new EnrollmentException("Apenas alunos podem atualizar progresso");
        }
        
        Student student = (Student) currentUser;
        Course course = courses.get(courseTitle);
        
        if (course == null) {
            throw new EnrollmentException("Curso não encontrado");
        }
        
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getCourse().equals(course))
                .findFirst()
                .orElseThrow(() -> new EnrollmentException("Aluno não está matriculado neste curso"));
        
        enrollment.setProgress(progress);
    }

    public void cancelEnrollment(String courseTitle) {
        if (!(currentUser instanceof Student)) {
            throw new EnrollmentException("Apenas alunos podem cancelar matrículas");
        }
        
        Student student = (Student) currentUser;
        Course course = courses.get(courseTitle);
        
        if (course == null) {
            throw new EnrollmentException("Curso não encontrado");
        }
        
        enrollments.removeIf(e -> e.getStudent().equals(student) && e.getCourse().equals(course));
    }

    // Sistema de Suporte
    public void createSupportTicket(String title, String message) {
        if (currentUser == null) {
            throw new EnrollmentException("Usuário não autenticado");
        }
        
        supportTickets.add(new SupportTicket(title, message, currentUser));
    }

    public SupportTicket processNextTicket() {
        if (!(currentUser instanceof Admin)) {
            throw new EnrollmentException("Apenas administradores podem processar tickets");
        }
        
        return supportTickets.poll();
    }

    public int getTicketQueueSize() {
        return supportTickets.size();
    }

    // Relatórios e Análises
    public List<Course> getCoursesByDifficulty(DifficultyLevel difficulty) {
        return courses.values().stream()
                .filter(course -> course.getDifficultyLevel() == difficulty)
                .sorted(Comparator.comparing(Course::getTitle))
                .collect(Collectors.toList());
    }

    public Set<String> getUniqueInstructors() {
        return courses.values().stream()
                .filter(course -> course.getStatus() == CourseStatus.ACTIVE)
                .map(Course::getInstructorName)
                .collect(Collectors.toSet());
    }

    public Map<String, List<Student>> getStudentsByPlan() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.groupingBy(
                    student -> student.getSubscriptionPlan().getPlanName()
                ));
    }

    public double getAverageProgress() {
        if (enrollments.isEmpty()) {
            return 0.0;
        }
        
        return enrollments.stream()
                .mapToInt(Enrollment::getProgress)
                .average()
                .orElse(0.0);
    }

    public Optional<Student> getStudentWithMostEnrollments() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .max(Comparator.comparing(student -> 
                    enrollments.stream()
                            .filter(e -> e.getStudent().equals(student))
                            .count()
                ));
    }

    // Exportação CSV
    public String exportToCsv(List<?> data, List<String> fields) {
        if (!(currentUser instanceof Admin)) {
            throw new EnrollmentException("Apenas administradores podem exportar dados");
        }
        
        return GenericCsvExporter.exportToCsv(data, fields);
    }
}
