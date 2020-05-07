package cz.vse.java.utils.emailServices;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EmailAddressTest {

    @Test
    public void okEmailValidation() {

        ArrayList<EmailAddress> ok = new ArrayList<>();

        ok.add(new EmailAddress("abc@abc.abc"));
        ok.add(new EmailAddress("ASD123@aFsd.AF"));
        ok.add(new EmailAddress("bhasdl@adf23.kaf"));

        int numberOfOk = 0;

        for (EmailAddress email : ok) {

            if(email.validate()){

                numberOfOk++;
            }
        }
        assertEquals(numberOfOk, ok.size());
    }

    @Test
    public void notOkEmailValidation() {

        ArrayList<String> notOk = new ArrayList<>();
        notOk.add("");
        notOk.add("asdf@@asdf.vaw");
        notOk.add("@asdf.vawev");
        notOk.add("asdf@asdfvawev");
        notOk.add("asdf@asdf.");
        notOk.add("asdfasdf@.vawev");
        notOk.add("adsfasdf.asdf");
        notOk.add("1234@1234.12");
        notOk.add("AWFE24>>");
        notOk.add("////");
        notOk.add("?:_%ˇ´+");

        int numOfNotOk = 0;

        for (String email : notOk) {

            try {
                EmailAddress emailAddress = new EmailAddress(email);

            } catch (IllegalArgumentException e){

                numOfNotOk++;
            }
        }
        assertEquals(numOfNotOk, notOk.size());
    }
}