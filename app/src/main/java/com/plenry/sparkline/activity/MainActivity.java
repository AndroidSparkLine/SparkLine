package com.plenry.sparkline.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.plenry.sparkline.R;
import com.plenry.sparkline.SearchBar;
import com.plenry.sparkline.adapter.RoomArrayAdapter;
import com.plenry.sparkline.bean.Room;
import com.plenry.sparkline.bean.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.loopj.android.http.*;
import com.plenry.sparkline.data.ImageHelper;

import org.json.*;

import cz.msebera.android.httpclient.Header;
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Room> rooms;
    private List<Room> filtered;
    private ImageView avatarImage;
    private TextView userName;
    private ListView lv;
    private User user;
    private Timer timer;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private SearchBar searchBar;
    private EditText searchEdit;
    private RoomArrayAdapter roomArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        userLogin();
        autoRefresh();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoRefresh();
    }

    public void searchBtn(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
    public void init() {
        setContentView(R.layout.activity_main);
        avatarImage = (ImageView) findViewById(R.id.avatarImageView);
        userName = (TextView) findViewById(R.id.usernameTv);
        lv = (ListView) findViewById(R.id.listView);
        searchEdit = (EditText) findViewById(R.id.searchEdit);
        lv.setOnItemClickListener(this);
        searchBar = (SearchBar) findViewById(R.id.searchBar);
        searchBar.bindWithListview(lv, SearchBar.BOTTOM);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        imageLoader.init(config);
        options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
    }

    public void performFilter() {
        String str = searchEdit.getText().toString();
        if (str.equals("")) {
            filtered = rooms;
        } else {
            filtered = new LinkedList<>();
            for (Room room : rooms) {
                if (room.getTopic().toLowerCase().contains(str.toLowerCase())) {
                    filtered.add(room);
                }
            }
        }
        roomArrayAdapter = new RoomArrayAdapter(this, R.layout.custom_row, filtered);
        lv.setAdapter(roomArrayAdapter);
    }

    public void userLogin() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams("deviceToken", getDeviceToken());
        client.post("https://plenry.com/rest/SLNewUser", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = new User(response.getString("name"), response.getString("photo"), response.getString("color"));
                    userName.setText(user.getName());
                    imageLoader.loadImage(user.getPhoto(), options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            avatarImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(loadedImage, 100));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void userChangeName(String name) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("deviceToken", getDeviceToken());
        params.put("userName", name);
        client.post("https://plenry.com/rest/SLChangeName", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = new User(response.getString("name"), response.getString("photo"), response.getString("color"));
                    userName.setText(user.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
        intent.putExtra("room", filtered.get(position));
        intent.putExtra("user", user);
        timer.cancel();
        startActivity(intent);
    }

    public void changeUserNameFunc(View v){
        final Dialog customDialog = new Dialog(MainActivity.this);
        customDialog.setTitle("Input a new name:");

        // inflate custom layout
        customDialog.setContentView(R.layout.change_name_dialog);
        final EditText nameInput = (EditText) customDialog.findViewById(R.id.enter_new_name);
        nameInput.setHint(user.getName());
        ((Button) customDialog.findViewById(R.id.save_name_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nameInput.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "Your name is empty", Toast.LENGTH_LONG).show();
                        } else {
                            userChangeName(nameInput.getText().toString());
                        }
                        customDialog.dismiss();
                    }
                });
        customDialog.show();
    }

    public void refreshList(List<Room> newRooms) {
        if (rooms != null && newRooms.size() != 0 && rooms.size() == newRooms.size() && rooms.get(rooms.size() - 1).getTopic().equals(newRooms.get(newRooms.size() - 1).getTopic())) return;
        rooms = newRooms;
        performFilter();
    }

    public void changeAvatarFunc(View v){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams("deviceToken", getDeviceToken());
        client.post("https://plenry.com/rest/SLChangePhoto", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    user = new User(response.getString("name"), response.getString("photo"), response.getString("color"));
                    userName.setText(user.getName());
                    imageLoader.loadImage(user.getPhoto(), options, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            avatarImage.setImageBitmap(ImageHelper.getRoundedCornerBitmap(loadedImage, 100));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createRoom(String topic) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("deviceToken", getDeviceToken());
        params.put("topic", topic);
        params.put("time", getTime());
        client.post("https://plenry.com/rest/SLCreateRoom", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                renewData();
            }
        });
    }

    public void addChatRoomFunc(View v){
        final Dialog addRoom_Dialog = new Dialog(MainActivity.this);
        addRoom_Dialog.setTitle("Create a new chat room:");
        addRoom_Dialog.setContentView(R.layout.add_chatroom_dialog);
        final EditText roomName = (EditText)addRoom_Dialog.findViewById(R.id.enter_chatroom_name);
        ((Button) addRoom_Dialog.findViewById(R.id.save_chatroom_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String roomName_added = roomName.getText().toString();
                        if(roomName_added.equals("")){
                            Toast.makeText(getApplicationContext(), "Your topic is empty!", Toast.LENGTH_LONG).show();
                            addRoom_Dialog.dismiss();
                            return;
                        }
                        for (Room r : rooms) {
                            if (r.getTopic().equals(roomName_added)) {
                                Toast.makeText(getApplicationContext(), "Topic already exists!", Toast.LENGTH_LONG).show();
                                addRoom_Dialog.dismiss();
                                return;
                            }
                        }
                        createRoom(roomName_added);
                        addRoom_Dialog.dismiss();
                    }
                });
        addRoom_Dialog.show();
    }

    public void renewData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams("deviceToken", getDeviceToken());
        client.post("https://plenry.com/rest/SLGetRooms", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                try {
                    List<Room> newRooms = new LinkedList<Room>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        if (checkTime(object.getString("time"))) {
                            newRooms.add(new Room(object.getString("topic"), object.getString("time"), object.getString("passcode"), object.getInt("size")));
                        }
                    }
                    refreshList(newRooms);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void autoRefresh() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        renewData();
                    }
                });
            }
        },0, 5000);
    }

    public boolean checkTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
        Date dateToCompare = sdf.parse(time);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        Date oneDayAgo = new Date(c.getTimeInMillis());
        return dateToCompare.after(oneDayAgo);
    }

    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
        return sdf.format(new Date());
    }

    public static String getDeviceToken() {
        return Settings.Secure.ANDROID_ID;
    }
}

