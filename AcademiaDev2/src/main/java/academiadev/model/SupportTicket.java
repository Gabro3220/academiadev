package academiadev.model;

public class SupportTicket {
    private String title;
    private String message;
    private User user;
    private int ticketId;
    private static int nextId = 1;

    public SupportTicket(String title, String message, User user) {
        this.title = title;
        this.message = message;
        this.user = user;
        this.ticketId = nextId++;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public int getTicketId() { return ticketId; }

    @Override
    public String toString() {
        return "SupportTicket{" +
                "id=" + ticketId +
                ", title='" + title + '\'' +
                ", user='" + user.getName() + '\'' +
                '}';
    }
}
