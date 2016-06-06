package com.plenry.sparkline.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.plenry.sparkline.R;
import com.plenry.sparkline.adapter.ChatMsgAdapter;
import com.plenry.sparkline.bean.Message;
import com.plenry.sparkline.bean.Room;
import com.plenry.sparkline.bean.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Xiaoyu on 5/16/16.
 */
public class ChatRoomActivity extends AppCompatActivity {

    private List<Message> messages;
    private ListView chatListView;
    private TextView topic_name_tv;
    private EditText msg_editor;
    private Room room;
    private User user;
    private Timer timer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        autoRefresh();
    }

    public void init(){
        setContentView(R.layout.chatroom_activity);
        chatListView = (ListView) findViewById(R.id.chatroom_listView);
        topic_name_tv = (TextView)findViewById(R.id.topic_name);
        msg_editor =(EditText) findViewById(R.id.et_sendmessage);
        room = (Room) getIntent().getSerializableExtra("room");
        topic_name_tv.setText(room.getTopic());
        user = (User) getIntent().getSerializableExtra("user");
        chatListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        chatListView.setStackFromBottom(true);
    }

    public void refreshData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("deviceToken", getDeviceToken());
        params.put("room", room.getTopic());
        client.post("https://plenry.com/rest/SLGetMessages", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Toast.makeText(getApplicationContext(), "" + jsonArray.length(), Toast.LENGTH_LONG);
                try {
                    List<Message> newMsg = new LinkedList<Message>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        newMsg.add(new Message(object.getString("content"), object.getString("time"), object.getString("userName"), object.getString("photo"), object.getString("color"), object.getString("room"), !object.getString("userName").equals(user.getName())));
                    }
                    refreshMsg(newMsg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshMsg(List<Message> msg) {
        if (messages != null && msg.size() != 0 && messages.size() == msg.size()) return;
        Collections.sort(msg, new Comparator<Message>() {
            @Override
            public int compare(Message lhs, Message rhs) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
                int ans = 0;
                try {
                    Date d1 = sdf.parse(lhs.getTime());
                    Date d2 = sdf.parse(rhs.getTime());
                    if (d1.after(d2)) ans = 1;
                    if (d2.after(d1)) ans = -1;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return ans;
            }
        });
        messages = msg;
        chatListView.setAdapter(new ChatMsgAdapter(this, 0, msg));
    }

    public void sendText(View v) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userName", user.getName());
        params.put("photo", user.getPhoto());
        params.put("color", user.getColor());
        params.put("time", getTime());
        params.put("content", msg_editor.getText().toString());
        params.put("room", room.getTopic());
        client.post("https://plenry.com/rest/SLSendMessage", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                msg_editor.setText("");
                refreshData();
            }
        });
    }

    public void autoRefresh() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                ChatRoomActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        refreshData();
                    }
                });
            }
        },0, 2000);
    }

    public void chat_back(View v){
        timer.cancel();
        onBackPressed();
        finish();
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
        return sdf.format(new Date());
    }

    public static String getDeviceToken() {
        return Settings.Secure.ANDROID_ID;
    }
}
