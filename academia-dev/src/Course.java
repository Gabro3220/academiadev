public class Course {
    private String title;
    private String description;
    private String instructorName;
    private int durationInHours;
    private DifficultyLevel difficultyLevel;
    private CourseStatus status;
    
    public Course(String title, String description, String instructorName, 
                  int durationInHours, DifficultyLevel difficultyLevel, CourseStatus status) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationInHours = durationInHours;
        this.difficultyLevel = difficultyLevel;
        this.status = status;
    }
    
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getInstructorName() { return instructorName; }
    public int getDurationInHours() { return durationInHours; }
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public CourseStatus getStatus() { return status; }
    public void setStatus(CourseStatus status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("%s - %s (%s) - %dh - %s", 
                title, instructorName, difficultyLevel, durationInHours, status);
    }
}
