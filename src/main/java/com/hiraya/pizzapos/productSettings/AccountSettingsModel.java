package com.hiraya.pizzapos.productSettings;

import java.io.IOException;
import java.net.URL;

import com.hiraya.pizzapos.App;
import com.hiraya.pizzapos.Toaster;
import com.hiraya.pizzapos.helpers.RestAPIHelper;
import com.hiraya.pizzapos.httpReqRes.SendRefreshTokenRequest;
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
        String tempImg;
        if (this.imageUrl == null) {
            tempImg = "";
        } else {
            tempImg = this.imageUrl.toExternalForm();
        }
        UserFields fields = new UserFields(
            this.displayName,
            this.email,
            this.contactNumber,
            tempImg,
            App.user.getLocalId()
        );
        App.bgThreads.submit(() -> {
            try {
                RestAPIHelper.updateUser(fields, App.user.getIdToken());
                var res = RestAPIHelper.updateProfile(req);
                var emailRes = RestAPIHelper.updateEmail(req2);
                // updateEmail resets user's tokens, good thing we
                // requested new tokens via returnSecureToken = true on the
                // update email request
                SendRefreshTokenRequest renewReq = new SendRefreshTokenRequest();
                renewReq.refresh_token = emailRes.refreshToken;
                var renew = RestAPIHelper.sendRToken(renewReq);
                App.user.setTokens(renew.refresh_token, renew.id_token, renew.access_token, App.user.getLocalId());
                if (res.error == null) {
                    Platform.runLater(() -> {
                        System.out.println("Update success");
                        Toaster.spawnToast("Profile updated.", "You will be logged out in 5s. Please relogin", "success");
                    });
                } else {
                    Platform.runLater(() -> {
                        System.out.println("Update failed");
                        System.out.println(res.error.code.toString());
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
