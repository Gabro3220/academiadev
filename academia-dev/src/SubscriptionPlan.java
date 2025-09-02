public abstract class SubscriptionPlan {
    public abstract boolean canEnroll(int currentEnrollments);
    public abstract String getPlanName();
}
