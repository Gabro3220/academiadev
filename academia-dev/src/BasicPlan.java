public class BasicPlan extends SubscriptionPlan {
    private static final int MAX_ENROLLMENTS = 3;
    
    @Override
    public boolean canEnroll(int currentEnrollments) {
        return currentEnrollments < MAX_ENROLLMENTS;
    }
    
    @Override
    public String getPlanName() {
        return "BASIC";
    }
}
