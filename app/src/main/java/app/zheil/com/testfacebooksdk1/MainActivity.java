package app.zheil.com.testfacebooksdk1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private ProfileTracker mProfileTracker;
    private ImageView iView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iView = findViewById(R.id.ivView);

        mCallbackManager = CallbackManager.Factory.create();
       // listeners();
        authFace();


    }

    public void authFace() {

       final LoginButton loginButton  = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");
       // loginButton.setReadPermissions("email");



        // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String profileImg = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large&width=1080";
                Picasso.get().load(profileImg).into(iView);

                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            printLog("Name is "+currentProfile.getLastName());
                            printLog(currentProfile.getId());
                            mProfileTracker.stopTracking();
                        }

                    };
                }
                printLog("OK !!!");
                printLog("Token!: " + loginResult.getAccessToken());
                Profile profile = Profile.getCurrentProfile();
               //printLog("name: " + Profile.getCurrentProfile().getName());


                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                } else {
                                    // get email and id of the user
                                    String email = me.optString("email");
                                    String id = me.optString("id");
                                    printLog("EMAIL: " + email );
                                }
                            }
                        }).executeAsync();



            }

            @Override
            public void onCancel() {
                printLog("CANCEL!");
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                printLog("ERROR !\r\n"+ exception.getMessage());
                // App code
            }
        });
    }


    private void printLog(String text) {
        Log.d("MYLOG", text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void listeners() {
        LoginManager.getInstance().registerCallback( mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });
    }
}
