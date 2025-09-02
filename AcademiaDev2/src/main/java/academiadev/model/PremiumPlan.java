package academiadev.model;

public class PremiumPlan implements SubscriptionPlan {
    
    @Override
    public boolean canEnroll(int currentEnrollments) {
        return true; // Número ilimitado
    }
    
    @Override
    public String getPlanName() {
        return "Premium";
    }
}
