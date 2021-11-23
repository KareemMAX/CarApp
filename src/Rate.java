public class Rate {
    Customer user;
    private float rateValue = 2.5f;

    Rate(Customer customer, float value) {
        this.user = customer;
        this.rateValue = value;
    }

    public float getRateValue() {
        return rateValue;
    }

    public Customer getUser() {
        return user;
    }

    @Override
    public String toString(){
        return user.getUserName() + " : " + String.valueOf(rateValue);
    }
}
