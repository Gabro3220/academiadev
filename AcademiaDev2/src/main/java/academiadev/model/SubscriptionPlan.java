package academiadev.model;

public interface SubscriptionPlan {
    boolean canEnroll(int currentEnrollments);
    String getPlanName();
}
