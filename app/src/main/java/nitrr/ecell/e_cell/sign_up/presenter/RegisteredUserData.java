package nitrr.ecell.e_cell.sign_up.presenter;

import java.util.ArrayList;

import nitrr.ecell.e_cell.sign_up.model.User;

public class RegisteredUserData {

    private ArrayList<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public boolean validateUserName(String UserName) {
        for (User user : users) {
            if (UserName.equals(user.getUserName()))
                return false;
        }

        return true;
    }
}