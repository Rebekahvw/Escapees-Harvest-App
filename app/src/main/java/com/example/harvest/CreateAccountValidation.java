package com.example.harvest;

public class CreateAccountValidation {

    /**
     * Checks to see if password is not 6 characters or longer
     *
     * @param password
     * @return greaterThan5
     */
    public boolean isValidPassword(String password){
        boolean greaterThan5 = password.length() > 5;

        return greaterThan5;
    }

    /**
     * Checks to see if FullName was filled
     *
     * @param fullName
     * @return fullNameFilled
     */
    public boolean isValidFullname(String fullName){
        boolean fullNameFilled = !fullName.isEmpty();

        return fullNameFilled;
    }

    /**
     * Checks to see if email was filled
     *
     * @param email
     * @return EmailFilled
     */
    public boolean isValidEmail(String email){
        boolean EmailFilled = !email.isEmpty();

        return EmailFilled;
    }


    /**
     * Checks to see if username was filled
     *
     * @param userName
     * @return userNameFilled
     */
    public boolean isValidUsername(String userName){
        boolean userNameFilled = !userName.isEmpty();

        return userNameFilled;
    }
}
