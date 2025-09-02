package academiadev.model;

public class BasicPlan implements SubscriptionPlan {
    
    @Override
    public boolean canEnroll(int currentEnrollments) {
        return currentEnrollments < 3;
    }
    
    @Override
    public String getPlanName() {
        return "Basic";
    }
}
