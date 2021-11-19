public class Admin extends Account
{
    public boolean ableToSignIn()
    {
        return false;
    }
    public Admin (String username, String password)
    {
        super(username,password);
    }

}
