package academiadev.model;

public class Enrollment {
    private Student student;
    private Course course;
    private int progress; // 0-100%

    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.progress = 0;
    }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public int getProgress() { return progress; }
    public void setProgress(int progress) { 
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progresso deve estar entre 0 e 100");
        }
        this.progress = progress; 
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "student='" + student.getName() + '\'' +
                ", course='" + course.getTitle() + '\'' +
                ", progress=" + progress + "%" +
                '}';
    }
}
