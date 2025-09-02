public class PremiumPlan extends SubscriptionPlan {
    @Override
    public boolean canEnroll(int currentEnrollments) {
        return true; 
    }
    
    @Override
    public String getPlanName() {
        return "PREMIUM";
    }
}
