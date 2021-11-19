import java.util.List;

public class Customer extends Account
{
    private String email="";
    private String phoneNumber="";
    private List<Offer> pastRides;

    public Customer(String userName, String password, String email, String phoneNumber)
    {
        super(userName,password);
        this.email =email;
        this.phoneNumber = phoneNumber;
    }
    public Customer(String userName, String password, String phoneNumber) //overload to make email optional
    {
        super(userName,password);
        this.phoneNumber = phoneNumber;
    }
    public void notify(Offer offer)
    {

    }


    boolean ableToSignIn()
    {
        return true;
    }
}
