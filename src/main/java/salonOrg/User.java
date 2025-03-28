package salonOrg;
import java.io.Serializable;

public class User implements Serializable{
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private int role_id;
    private String roleName;
    private int access;

    public User() {
        this.login = "";
        this.password = "";
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.roleName = "";
        this.role_id = 0;
        this.access = 0;
    }

    public User(String login, String password, String firstName, String lastName, String email, int role_id, int access) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role_id = role_id;
        this.access = access;
    }

    @Override
    public String  toString(){
        return "login " + login + "\npassword " + password + "\nfifirstName "
                + firstName + "\nlastName " + lastName + "\nemail " + email
                + "\nrole_id " + role_id + "\naccess " + access;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }
}
