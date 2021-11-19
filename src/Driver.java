import java.util.List;

public class Driver extends Account
{
    private String email="";
    private String phoneNumber="";
    private String nationalID ="";
    private String licence = "";
    private boolean verified= false;
    private List<String> favouriteAreas;
    private List<Rate> rates;

    public Driver(String userName, String password, String email, String phoneNumber, String licence, String nationalID)
    {
        super(userName,password);
        this.email =email;
        this.phoneNumber = phoneNumber;
        this.licence = licence;
        this.nationalID = nationalID;
    }
    //overload to make email optional
    public Driver(String userName, String password, String phoneNumber, String licence, String nationalID)
    {
        super(userName,password);
        this.phoneNumber = phoneNumber;
        this.licence = licence;
        this.nationalID = nationalID;
    }

    public boolean ableToSignIn()
    {
        return true;
    }
    public void setVerified(boolean verified)
    {
        this.verified = verified;
    }
    public void notify(Request request)
    {

    }
    public void rate (Customer customer, float value)
    {
        rates.add(new Rate(customer,value));
    }
    public List<Rate> getRates()
    {
        return rates;
    }
    public float getAverageRate(){
        int sum = 0;
        for (Rate rate : rates)
        {
            sum+= rate.getRateValue();
        }
        return sum/rates.size();
    }
    public void addFavouriteArea(String newArea)
    {
        favouriteAreas.add(newArea);
    }
    public void deleteFavouriteArea(String area)
    {
        favouriteAreas.remove(area);
    }


}
