package ad.ss.df.a;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class Facetoface_mainActivity extends Activity {
    /** Called when the activity is first created. */
	
	// Facebook objects
	Facebook facebook;
	SharedPreferences sharedPref;
	AsyncFacebookRunner asyncRunner;
	
	// Views
	TabHost tab;
	Button facebookConnBtn;
	ListView friendList;
	ImageView userProfilePic;
	TextView userProfileName;
	TextView userProfileGrade;
	TextView itemTime;
	TabWidget tabwid;
	TextView FriendsTab;
	TextView GameFriendsTab;
	TextView InviteTab;
	
    //---------------------//
    //       On Create     //
    //---------------------//
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		///////////////////////
		//  Find view by id  //
		///////////////////////
        tab = (TabHost) findViewById(R.id.tabhost);
        facebookConnBtn = (Button) findViewById(R.id.Button_FacebookConn);
        friendList = (ListView) findViewById(R.id.List_friendList);
        userProfilePic = (ImageView) findViewById(R.id.Image_ProfileImg);
        userProfileName = (TextView) findViewById(R.id.Text_userName);
        userProfileGrade = (TextView) findViewById(R.id.Text_userGrade);
        itemTime = (TextView) findViewById(R.id.Text_itemTime);
        
		userProfileName.setTextSize(getResources().getDisplayMetrics().density * 14 + 0.5f);
		itemTime.setTextSize(getResources().getDisplayMetrics().density * 8 + 0.5f);
		userProfileName.setOnClickListener(new TextView.OnClickListener() { //if user name clicked, show the name with moving
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(userProfileName.isSelected())
					userProfileName.setSelected(false);
				else
					userProfileName.setSelected(true);
			}
		});
		userProfileGrade.setTextSize(getResources().getDisplayMetrics().density * 12 + 0.5f);
		
		///////////////////////
		//  Tab Initialize   //
		///////////////////////
        tab.setup();
        
        TabHost.TabSpec spec1 = tab.newTabSpec("friends");
        TabHost.TabSpec spec2 = tab.newTabSpec("game friends");
        TabHost.TabSpec spec3 = tab.newTabSpec("invite");

        LayoutInflater layout = getLayoutInflater();
        
        View v1 = layout.inflate(R.layout.tab,null);
        View v2 = layout.inflate(R.layout.tab,null);
        View v3 = layout.inflate(R.layout.tab,null);
        
        //set Friends tab
        FriendsTab = (TextView) v1.findViewById(R.id.TextView_Tab);
        spec1.setIndicator(v1);
        FriendsTab.setText("Freinds");
        FriendsTab.setBackgroundResource(R.drawable.main_tab_press);
        FriendsTab.setTextColor(Color.WHITE);
        
        //set Game friends tab
        GameFriendsTab = (TextView) v2.findViewById(R.id.TextView_Tab);
        spec2.setIndicator(v2);
        GameFriendsTab.setText("Game Friends");
        GameFriendsTab.setTextColor(Color.DKGRAY);
        
        //set Invite tab
        InviteTab = (TextView) v3.findViewById(R.id.TextView_Tab);
        spec3.setIndicator(v3);
        InviteTab.setText("Invite");
        InviteTab.setTextColor(Color.DKGRAY);
        
        //set tab's content
        spec1.setContent(R.id.List_friendList);
        spec2.setContent(R.id.test);
        spec3.setContent(R.id.test);

        tab.addTab(spec1);
        tab.addTab(spec2);
        tab.addTab(spec3);

        FrameLayout f = tab.getTabContentView();
        f.setPadding(0, (int) (getResources().getDisplayMetrics().density * 30), 0, 0);
    
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() { //set tab's image when selected
			
			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				
				if(tabId.equals("friends")){
					FriendsTab.setBackgroundResource(R.drawable.main_tab_press);
					FriendsTab.setTextColor(Color.WHITE);
					GameFriendsTab.setBackgroundResource(R.drawable.main_tab);
					GameFriendsTab.setTextColor(Color.DKGRAY);
					InviteTab.setBackgroundResource(R.drawable.main_tab);	
					InviteTab.setTextColor(Color.DKGRAY);
				}
				else if(tabId.equals("game friends")){
					FriendsTab.setBackgroundResource(R.drawable.main_tab);
					FriendsTab.setTextColor(Color.DKGRAY);
					GameFriendsTab.setBackgroundResource(R.drawable.main_tab_press);
					GameFriendsTab.setTextColor(Color.WHITE);
					InviteTab.setBackgroundResource(R.drawable.main_tab);	
					InviteTab.setTextColor(Color.DKGRAY);
				}
				else if(tabId.equals("invite")){
					FriendsTab.setBackgroundResource(R.drawable.main_tab);
					FriendsTab.setTextColor(Color.DKGRAY);
					GameFriendsTab.setBackgroundResource(R.drawable.main_tab);
					GameFriendsTab.setTextColor(Color.DKGRAY);
					InviteTab.setBackgroundResource(R.drawable.main_tab_press);	
					InviteTab.setTextColor(Color.WHITE);
				}
			}
		});

		///////////////////////
		// Facebook connect  //
		///////////////////////
        facebook = new Facebook(Config.APP_ID);
        asyncRunner = new AsyncFacebookRunner(facebook);
        

	    sharedPref = getPreferences(MODE_PRIVATE);
	    String accessToken = sharedPref.getString("access_token", null);
	    long expires = sharedPref.getLong("aceess_expires", 0);
	        
	    //check access token && expires
	    if(accessToken != null)
	        facebook.setAccessToken(accessToken);
	    if(expires != 0)
	        facebook.setAccessExpires(expires);
	        
	    //get facebook profile, etc...
	    if(facebook.isSessionValid()){
	    	setInformations(facebook, userProfilePic, userProfileName, asyncRunner, friendList,facebookConnBtn);
	        	
	    	//set facebook connect button image to on
	       	Resources res = getResources();
	        BitmapDrawable d = (BitmapDrawable) res.getDrawable(R.drawable.profile_f_on);
	        facebookConnBtn.setBackgroundDrawable(d);
	    }
	    else{
	        if(accessToken != null && expires != 0){ //if access token and exipires is exist but session is not valid - retry
				String[] permissions = {"user_status","user_about_me", "offline_access", "publish_stream", "user_photos", "publish_checkins", "friends_relationships", "user_relationships", "friends_relationship_details", "user_relationship_details" };
				facebook.authorize(Facetoface_mainActivity.this,permissions, new AuthDialogListener(sharedPref, facebook,facebookConnBtn,userProfilePic, userProfileName, asyncRunner, friendList,Facetoface_mainActivity.this));

				//set facebook connect button image to on
				Resources res = getResources();
		        BitmapDrawable d = (BitmapDrawable) res.getDrawable(R.drawable.profile_f_on);
		        facebookConnBtn.setBackgroundDrawable(d);   
	    }

  }    
///////////////////////////////////////////////// I N N E R  M E T H O D S  /////////////////////////////////////////////////////////////
        
    //---------------------//
    // Facebook connection //
    //---------------------//
    facebookConnBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!facebook.isSessionValid()){ 
					//not logged in - connect facebook
					String[] permissions = {"user_status","user_about_me", "offline_access", "publish_stream", "user_photos", "publish_checkins", "friends_relationships", "user_relationships", "friends_relationship_details", "user_relationship_details" };
					facebook.authorize(Facetoface_mainActivity.this,permissions, new AuthDialogListener(sharedPref, facebook,facebookConnBtn,userProfilePic, userProfileName, asyncRunner, friendList,Facetoface_mainActivity.this));
	
					//set facebook connect button image to on
					Resources res = getResources();
			        BitmapDrawable d = (BitmapDrawable) res.getDrawable(R.drawable.profile_f_on);
			        facebookConnBtn.setBackgroundDrawable(d);   
				}
		       }
		});
    }
    
    //---------------------------//
    // Facebook authorize result //
    //---------------------------//
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(!facebook.isSessionValid()) 
			facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
    //-----------------------------------------------//
    // Set user information and friend's information //
    //-----------------------------------------------//
	public void setInformations(Facebook facebook, ImageView userProfilePic, TextView userProfileName, AsyncFacebookRunner asyncRunner, ListView friendList, Button facebookConnBtn){
		Bundle params = new Bundle();
        params.putString("fields", "name, picture");
		JSONObject json;
		try {
			//get my facebook picture
			json = Util.parseJson(facebook.request("me", params));
			
			URL url = new URL(json.getString("picture"));
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			Bitmap bm = BitmapFactory.decodeStream(bis);
			bm = ImageHelper.getRoundedCornerBitmap(bm,5);

			userProfilePic.setImageBitmap(bm); //set profile picture
			userProfileName.setText(json.getString("name")); //set profile name

			//get friends list
			Bundle Fparams = new Bundle();
            Fparams.putString("fields", "name, picture");
            final FriendListRequestListener listener = new FriendListRequestListener(Facetoface_mainActivity.this, friendList);
            asyncRunner.request("me/friends",Fparams,listener);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
}