package windyoak.core;

/**
 *
 * @author Felix Haller
 */
class User {
    
    private String username;
    private String forename;
    private String surname;

    public String getUsername()
    {
        return username;
    }

    public String getForename()
    {
        return forename;
    }

    public String getSurname()
    {
        return surname;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setForename(String forename)
    {
        this.forename = forename;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

}
