public class Rate
{
    private float rateValue=2.5f;
    Customer user;

    Rate(Customer customer, float value)
    {
        this.user = customer;
        this.rateValue = value;
    }

    public float getRateValue()
    {
        return rateValue;
    }
}
