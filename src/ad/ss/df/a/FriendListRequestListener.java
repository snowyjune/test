package ad.ss.df.a;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.android.FacebookError;
import com.facebook.android.R;
import com.facebook.android.AsyncFacebookRunner.RequestListener;

public class FriendListRequestListener implements RequestListener {

	Context context;
	ListView friend_list;
	JSONArray jsonArray; 
	
	public FriendListRequestListener(Context context, ListView friend_list) {
		super();
		this.context = context;
		this.friend_list = friend_list;
	}
	
	public JSONArray getJsonArray(){
		return jsonArray;
	}

	@Override
	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub
		Log.i("RESPONSE", response);
		try {

			jsonArray = new JSONObject(response).getJSONArray("data");
			
			((Activity) context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					friend_list.setAdapter(new FriendAdapter(jsonArray, context));
				}
			});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onIOException(IOException e, Object state) {}

	@Override
	public void onFileNotFoundException(FileNotFoundException e,
			Object state) {}

	@Override
	public void onMalformedURLException(MalformedURLException e,
			Object state) {}

	@Override
	public void onFacebookError(FacebookError e, Object state) {}
	

	 public  class FriendAdapter extends BaseAdapter{
	        private LayoutInflater mInflater;
	        JSONArray jsonArray;
	        Context context;
	        
			public FriendAdapter(JSONArray jsonArray, Context c) {
				super();
				this.context = c;
				this.jsonArray = jsonArray;
	            if (Utility.model == null) {
	                Utility.model = new FriendsGetProfilePics();
	            }
	            Utility.model.setListener(this);
	            mInflater = LayoutInflater.from(context);
			}

			@Override
			public int getCount() {return jsonArray.length();}

			@Override
			public Object getItem(int position) {return null;}

			@Override
			public long getItemId(int position) {return 0;}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

		        JSONObject jsonObject = null;
		            try {
		                jsonObject = jsonArray.getJSONObject(position);
		            } catch (JSONException e) {
		                e.printStackTrace();
		            }
		      
		            View hView = convertView;
		            
		            if (convertView == null) {
		                hView = mInflater.inflate(R.layout.friend_item, null);
		                FriendListItem holder = new FriendListItem();
		                holder.profile_pic = (ImageView) hView.findViewById(R.id.FriendPicImg);
		                holder.name = (TextView) hView.findViewById(R.id.FriendName);

		                hView.setTag(holder);
		            }
		            
		            FriendListItem holder = (FriendListItem) hView.getTag();
		            
		            try {
						holder.profile_pic.setImageBitmap(Utility.model.getImage(jsonObject.getString("id"), jsonObject.getString("picture")));

						Log.i("PIC", jsonObject.toString());
						holder.name.setText(jsonObject.getString("name"));
						
						holder.name.setTextSize(this.context.getResources().getDisplayMetrics().density * 12 + 0.5f);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		            
		            return hView;
			}
	    }
}
