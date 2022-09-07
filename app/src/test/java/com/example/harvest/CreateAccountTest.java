package com.example.harvest;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CreateAccountTest {

    @Test
    public void aValidPasswordPasses() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(utils.isValidPassword("123456"));
    }

    @Test
    public void aValidPasswordFails() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(!utils.isValidPassword("12356"));
    }

    @Test
    public void aValidFullNamePasses() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(utils.isValidFullname("name"));
    }

    @Test
    public void aValidFullNameFails() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(!utils.isValidFullname(""));
    }

    @Test
    public void aValidEmailPasses() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(utils.isValidEmail("123@123"));
    }

    @Test
    public void aValidEmailFails() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(!utils.isValidEmail(""));
    }

    @Test
    public void aValidUserNamePasses() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(utils.isValidUsername("123123"));
    }

    @Test
    public void aValidUserNameFails() throws Exception{

        CreateAccountValidation utils = new CreateAccountValidation();

        assertTrue(!utils.isValidUsername(""));
    }
}