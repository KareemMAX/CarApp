import java.util.List;

public class Driver extends Account {
    private String email = "";
    private String phoneNumber = "";
    private String nationalID = "";
    private String license = "";
    private boolean verified = false;
    private List<String> favouriteAreas;
    private List<Rate> rates;

    public Driver(String userName, String password, String email, String phoneNumber, String licence, String nationalID) {
        super(userName, password);
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.license = licence;
        this.nationalID = nationalID;
    }

    //overload to make email optional
    public Driver(String userName, String password, String phoneNumber, String licence, String nationalID) {
        super(userName, password);
        this.phoneNumber = phoneNumber;
        this.license = licence;
        this.nationalID = nationalID;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNationalID() {
        return nationalID;
    }

    public String getLicense() {
        return license;
    }

    public boolean isVerified() {
        return verified;
    }

    public List<String> getFavouriteAreas() {
        return favouriteAreas;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public boolean ableToSignIn() {
        return !suspended && verified;
    }

    public void verify() {
        this.verified = true;
    }

    public void notify(Request request) {

    }

    public void rate(Customer customer, float value) {
        rates.add(new Rate(customer, value));
    }

    public float getAverageRate() {
        int sum = 0;
        for (Rate rate : rates) {
            sum += rate.getRateValue();
        }
        return (float) sum / (float) rates.size();
    }

    public void addFavouriteArea(String newArea) {
        favouriteAreas.add(newArea);
    }

    public void deleteFavouriteArea(String area) {
        favouriteAreas.remove(area);
    }
}
