package ad.ss.df.a;

import android.widget.ImageView;
import android.widget.TextView;

public class FriendListItem {
    ImageView profile_pic;
    TextView name;
    
    public String getName(){
    	return (String) name.getText();
    }
}
