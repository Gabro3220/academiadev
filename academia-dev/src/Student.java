public class Student extends User {
    private SubscriptionPlan subscriptionPlan;
    
    public Student(String name, String email, SubscriptionPlan subscriptionPlan) {
        super(name, email);
        this.subscriptionPlan = subscriptionPlan;
    }
    
    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }
    
    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }
}
