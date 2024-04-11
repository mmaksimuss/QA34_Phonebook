package com.phonebook.tests;

import com.phonebook.data.ContactData;
import com.phonebook.data.UserData;
import com.phonebook.models.Contact;
import com.phonebook.models.User;
import com.phonebook.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AddContactTests extends TestBase {
    @BeforeMethod
    public void ensurePrecondition() {
        if (!app.getUser().isLoginLinkPresent()) {
            app.getUser().clickOnSignOutButton();
        }
        app.getUser().clickOnLoginLink();
        app.getUser().fillLoginRegisterForm(new User().setEmail(UserData.EMAIL)
                .setPassword(UserData.PASSWORD));
        app.getUser().clickOnLoginButton();
    }

    @Test
    public void addContactPositiveTest() {
        //click on add link
        //enter name
        app.getContact().clickOnAddLink();
        app.getContact().fillAddContactForm(new Contact()
                .setName(ContactData.NAME)
                .setLastName(ContactData.LAST_NAME)
                .setPhone(ContactData.PHONE)
                .setEmail(ContactData.CONTACT_EMAIL)
                .setAdress(ContactData.ADRESS)
                .setDescription(ContactData.DESC));
        // click on save button
        app.getContact().clickOnSaveButton();
        Assert.assertTrue(app.getContact().isContactCreated(ContactData.NAME));
    }


    @AfterMethod
    public void postCondition() {
        app.getContact().removeContact();
    }



    @Test(dataProvider = "addNewContact",dataProviderClass = DataProviders.class)
    public void addContactPositiveTestFromDataProvider(String name, String lastname, String phone,
                                                       String email, String adress, String description) {
        app.getContact().clickOnAddLink();
        app.getContact().fillAddContactForm(new Contact()
                .setName(name)
                .setLastName(lastname)
                .setPhone(phone)
                .setEmail(email)
                .setAdress(adress)
                .setDescription(description));
        // click on save button
        app.getContact().clickOnSaveButton();
        Assert.assertTrue(app.getContact().isContactCreated(name));
    }

    @DataProvider
    public Iterator<Object[]> addNewContactFromCsvFile() throws IOException {
        List<Object[]> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(("src/test/resources/Contact.csv")));
        String line = reader.readLine();
        while (line != null) {
            String[] split = line.split(",");
            list.add(new Object[]{new Contact().setName(split[0]).setLastName(split[1]).setPhone(split[2])
                    .setEmail(split[3]).setAdress(split[4]).setDescription(split[5])});
            line = reader.readLine();
        }
        return list.iterator();
    }

    @Test(dataProvider = "addNewContactFromCsvFile",dataProviderClass = DataProviders.class )
    public void addContactPositiveTestFromDataProviderWithCsvFile(Contact contact) {
        app.getContact().clickOnAddLink();
        app.getContact().fillAddContactForm(contact);
        app.getContact().clickOnSaveButton();
        Assert.assertTrue(app.getContact().isContactCreated(contact.getName()));
    }
}
