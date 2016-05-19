package com.plenry.sparkline.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.plenry.sparkline.R;
import com.plenry.sparkline.bean.Room;
import com.plenry.sparkline.bean.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    List<Room> rooms;
    private ImageView avatarImage;
    private TextView userName;
    private ImageView addChatRoom;
    private ListView lv;
    private User user;

    final String myPreference = "userName-preference";
    // create a reference to the shared preferences object
    SharedPreferences mySharedPreferences;
    // obtain an editor to add data to my SharedPreferences object
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String DemoUrl = "https://www.petfinder.com/wp-content/uploads" +
                "/2012/11/140272627-grooming-needs-senior-cat-632x475.jpg";
        user = new User("Lily", DemoUrl, "green");
        avatarImage = (ImageView) findViewById(R.id.avatarImageView);
        userName = (TextView) findViewById(R.id.usernameTv);
        userName.setText(user.getName());
        addChatRoom = (ImageView)findViewById(R.id.add_chatroom_ImageView);
        rooms = new ArrayList<>();
        rooms.add(new Room("Election", "1970-01-01T00:00:00.000+0000", "1234"));
        rooms.add(new Room("Asian girls", "1970-01-01T00:00:00.000+0000", "1234"));
        rooms.add(new Room("Stocks", "1970-01-01T00:00:00.000+0000", "1234"));
        rooms.add(new Room("Education", "1970-01-01T00:00:00.000+0000", "1234"));
        lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(new RoomArrayAdapter(this, R.layout.custom_row, rooms));
        lv.setOnItemClickListener(this);
        // mySharedPreferences = getSharedPreferences(myPreference, Activity.MODE_PRIVATE);
        mySharedPreferences = getPreferences(Activity.MODE_PRIVATE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Room room = rooms.get(position);
        Toast.makeText(getApplicationContext(), "Topic clicked : " + room.getTopic(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
        intent.putExtra("topicName", room.getTopic());
        intent.putExtra("numParticipants", room.getSize());
        startActivity(intent);
    }

    public void changeUserNameFunc(View v){
        final Dialog customDialog = new Dialog(MainActivity.this);
        customDialog.setTitle("Input your New Name");

        // inflate custom layout
        customDialog.setContentView(R.layout.change_name_dialog);
        final EditText nameInput = (EditText) customDialog.findViewById(R.id.enter_new_name);
        nameInput.setText(mySharedPreferences.getString("name", ""));
        ((Button) customDialog.findViewById(R.id.save_name_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myEditor =mySharedPreferences.edit();
                        myEditor.putString("name", nameInput.getText().toString());
                        myEditor.commit();
                        userName.setText(mySharedPreferences.getString("name", ""));
                        nameInput.setText("");
                        refreshList();
                        customDialog.dismiss();
                        //todo add avatar, color to SharedPreferences too
                    }
                });
        customDialog.show();
    }

    public void refreshList() {
//        lv.setAdapter(new RoomArrayAdapter(this, R.layout.custom_row, rooms));
    }
    public void changeAvatarFunc(View v){
        //change the avatar for the user by assigning a new avatar and userName to this user
    }

    public void addChatRoomFunc(View v){
        //pop out a dialog to add a chatRoom, this chatRoom will be added to the chatRoom database
        final Dialog addRoom_Dialog = new Dialog(MainActivity.this);
        addRoom_Dialog.setTitle("Create a new chatroom");
        // inflate custom layout
        addRoom_Dialog.setContentView(R.layout.add_chatroom_dialog);
        final EditText roomName = (EditText)addRoom_Dialog.findViewById(R.id.enter_chatroom_name);
        final String roomName_added = roomName.getText().toString();
        ((Button) addRoom_Dialog.findViewById(R.id.save_chatroom_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String roomName_added = roomName.getText().toString();
                        if(!roomName_added.equals("")){
                            Room topicAdded = new Room(roomName_added, "1970-01-01T00:00:00.000+0000", "1234" );
                            rooms.add(topicAdded);

                        }

                        addRoom_Dialog.dismiss();
                    }
                });
        addRoom_Dialog.show();
    }
}

