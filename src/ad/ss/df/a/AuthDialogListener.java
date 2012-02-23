package ad.ss.df.a;

import java.io.BufferedInputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;

public class AuthDialogListener implements DialogListener {

	private SharedPreferences sharedPref;
	private Facebook facebook;
	private Button login_btn;
	private ImageView user_img;
	private TextView name;
	private AsyncFacebookRunner async;
	private ListView list;
	private Facetoface_mainActivity activity;

	public AuthDialogListener(SharedPreferences sharedPref,Facebook facebook,Button btn,ImageView img, TextView name, AsyncFacebookRunner async, ListView list, Facetoface_mainActivity activity) {
		super();
		this.sharedPref = sharedPref;
		this.facebook = facebook;
		login_btn = btn;
		user_img = img;
		this.async = async;
		this.name = name;
		this.list = list;
		this.activity = activity;
	}

	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		Log.i("onComplete", "authComplete");
		
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("access_token", facebook.getAccessToken());
		editor.putLong("access_expires", facebook.getAccessExpires());
        editor.commit();
        
        try {
			Bundle params = new Bundle();
            params.putString("fields", "name, picture");
			JSONObject json = Util.parseJson(facebook.request("me", params));   

			Log.i("ME",  Util.parseJson(facebook.request("me")).toString());
			Log.i("PIC", json.getString("picture"));

			URL url = new URL(json.getString("picture"));
			URLConnection conn = url.openConnection();
			conn.connect();
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			Bitmap bm = BitmapFactory.decodeStream(bis);
			
			user_img.setImageBitmap(bm);
			name.setText(json.getString("name"));
			
			Bundle Fparams = new Bundle();
            Fparams.putString("fields", "name, picture");
            FriendListRequestListener listener = new FriendListRequestListener(activity, list);
            async.request("me/friends",Fparams,listener);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onFacebookError(FacebookError e) {}

	@Override
	public void onError(DialogError e) {}

	@Override
	public void onCancel() {}

}
