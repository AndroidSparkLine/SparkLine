package com.plenry.sparkline.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.plenry.sparkline.R;
import com.plenry.sparkline.bean.Message;
import com.plenry.sparkline.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xiaoyu on 5/16/16.
 */
public class ChatRoomActivity extends AppCompatActivity {

    List<Message> messages;
    private ListView chatListView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);
        messages = new ArrayList<>();
        String url1 = "http://java.sogeti.nl/JavaBlog/wp-content/uploads" +
                "/2009/04/android_icon_256.png";
        User user1 = new User("John",url1, "red");
        Message message1 = new Message("This is a test", "15:00pm", user1);
        messages.add(message1);
//        messages.add(message2);
        chatListView = (ListView) findViewById(R.id.chatroom_listView);
        chatListView.setAdapter(new ChatAdapter(this, R.layout.message_layout, messages));
    }
}
