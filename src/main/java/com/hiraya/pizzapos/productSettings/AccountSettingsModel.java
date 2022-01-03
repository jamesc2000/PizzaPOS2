package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.UpdateEmailRequest;
import com.hiraya.pizzapos.httpReqRes.UserFields;
import com.hiraya.pizzapos.httpReqRes.UserProfileRequest;

import javafx.application.Platform;

public class AccountSettingsModel {
    private String email;
    private String displayName;
    private URL imageUrl;
    private String contactNumber;

    private String oldPassword;
    private String newPassword;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public URL getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        try {
            this.imageUrl = new URL(imageUrl);
        } catch (Exception e) {
            this.imageUrl = null;
        }
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void sendToFirebase() {
        UserProfileRequest req = new UserProfileRequest(
            App.user.getIdToken(),
            this.displayName,
            "",
            null // TODO: Profile pic
        );
        UpdateEmailRequest req2 = new UpdateEmailRequest(
            App.user.getIdToken(),
            this.email
        );
        UserFields fields = new UserFields(
            this.displayName,
            this.email,
            this.contactNumber,
            App.user.getLocalId()
        );
        App.bgThreads.submit(() -> {
            try {
                var res = RestAPIHelper.updateProfile(req);
                RestAPIHelper.updateEmail(req2);
                RestAPIHelper.updateUser(fields, App.user.getIdToken());
                if (res.error == null) {
                    Platform.runLater(() -> {
                        System.out.println("Update success");
                        Toaster.spawnToast("Profile updated.", "", "success");
                    });
                } else {
                    Platform.runLater(() -> {
                        System.out.println("Update failed");
                        Toaster.spawnToast("Profile not updated", res.error.message, "error");
                    });
                }
            } catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void getFromFirebase() {
        try {
            UserFields rawData = RestAPIHelper.getUserData();
            this.displayName = rawData.name.stringValue;
            this.email = rawData.email.stringValue;
            this.contactNumber = rawData.contact.stringValue;
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
