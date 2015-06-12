package com.avoscloud.chat.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.avoscloud.chat.R;
import com.avoscloud.chat.service.CacheService;
import com.avoscloud.chat.service.PreferenceMap;
import com.avoscloud.chat.service.UpdateService;
import com.avoscloud.chat.service.UserService;
import com.avoscloud.chat.service.event.LoginFinishEvent;
import com.avoscloud.chat.ui.base_activity.BaseActivity;
import com.avoscloud.chat.ui.contact.ContactFragment;
import com.avoscloud.chat.ui.conversation.ConversationRecentFragment;
import com.avoscloud.chat.ui.discover.DiscoverFragment;
import com.avoscloud.chat.ui.profile.ProfileFragment;
import com.avoscloud.chat.util.Logger;
import com.avoscloud.leanchatlib.controller.ChatManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import de.greenrobot.event.EventBus;

/**
 * Created by lzw on 14-9-17.
 */
public class MainActivity extends BaseActivity {
    public static final int FRAGMENT_N = 4;
    public static final int[] tabsNormalBackIds = new int[]{R.drawable.tabbar_chat,
            R.drawable.tabbar_contacts, R.drawable.tabbar_discover, R.drawable.tabbar_me};
    public static final int[] tabsActiveBackIds = new int[]{R.drawable.tabbar_chat_active,
            R.drawable.tabbar_contacts_active, R.drawable.tabbar_discover_active,
            R.drawable.tabbar_me_active};
    private static final String FRAGMENT_TAG_CONVERSATION = "conversation";
    private static final String FRAGMENT_TAG_CONTACT = "contact";
    private static final String FRAGMENT_TAG_DISCOVER = "discover";
    private static final String FRAGMENT_TAG_PROFILE = "profile";
    private static final String[] fragmentTags = new String[]{FRAGMENT_TAG_CONVERSATION, FRAGMENT_TAG_CONTACT,
            FRAGMENT_TAG_DISCOVER, FRAGMENT_TAG_PROFILE};

    public LocationClient locClient;
    public MyLocationListener locationListener;
    Button conversationBtn, contactBtn, discoverBtn, mySpaceBtn;
    View fragmentContainer;
    ContactFragment contactFragment;
    DiscoverFragment discoverFragment;
    ConversationRecentFragment conversationRecentFragment;
    ProfileFragment profileFragment;
    Button[] tabs;
    View recentTips, contactTips;

    public static void goMainActivityFromActivity(Activity fromActivity) {
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new LoginFinishEvent());

        ChatManager chatManager = ChatManager.getInstance();
        chatManager.setupDatabaseWithSelfId(AVUser.getCurrentUser().getObjectId());
        chatManager.openClientWithSelfId(AVUser.getCurrentUser().getObjectId(), null);
        Intent intent = new Intent(fromActivity, MainActivity.class);
        fromActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findView();
        init();

        //mySpaceBtn.performClick();
        //contactBtn.performClick();
        conversationBtn.performClick();
        //discoverBtn.performClick();
        initBaiduLocClient();

        CacheService.registerUser(AVUser.getCurrentUser());
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateService updateService = UpdateService.getInstance(this);
        updateService.checkUpdate();
    }

    private void initBaiduLocClient() {
        locClient = new LocationClient(this.getApplicationContext());
        locClient.setDebug(true);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(5000);
        option.setIsNeedAddress(false);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        locClient.setLocOption(option);

        locationListener = new MyLocationListener();
        locClient.registerLocationListener(locationListener);
        locClient.start();
    }

    private void init() {
        tabs = new Button[]{conversationBtn, contactBtn, discoverBtn, mySpaceBtn};
    }

    private void findView() {
        conversationBtn = (Button) findViewById(R.id.btn_message);
        contactBtn = (Button) findViewById(R.id.btn_contact);
        discoverBtn = (Button) findViewById(R.id.btn_discover);
        mySpaceBtn = (Button) findViewById(R.id.btn_my_space);
        fragmentContainer = findViewById(R.id.fragment_container);
        recentTips = findViewById(R.id.iv_recent_tips);
        contactTips = findViewById(R.id.iv_contact_tips);
    }

    public void onTabSelect(View v) {
        int id = v.getId();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        setNormalBackgrounds();
        if (id == R.id.btn_message) {
            if (conversationRecentFragment == null) {
                conversationRecentFragment = new ConversationRecentFragment();
                transaction.add(R.id.fragment_container, conversationRecentFragment, FRAGMENT_TAG_CONVERSATION);
            }
            transaction.show(conversationRecentFragment);
        } else if (id == R.id.btn_contact) {
            if (contactFragment == null) {
                contactFragment = new ContactFragment();
                transaction.add(R.id.fragment_container, contactFragment, FRAGMENT_TAG_CONTACT);
            }
            transaction.show(contactFragment);
        } else if (id == R.id.btn_discover) {
            if (discoverFragment == null) {
                discoverFragment = new DiscoverFragment();
                transaction.add(R.id.fragment_container, discoverFragment, FRAGMENT_TAG_DISCOVER);
            }
            transaction.show(discoverFragment);
        } else if (id == R.id.btn_my_space) {
            if (profileFragment == null) {
                profileFragment = new ProfileFragment();
                transaction.add(R.id.fragment_container, profileFragment, FRAGMENT_TAG_PROFILE);
            }
            transaction.show(profileFragment);
        }
        int pos;
        for (pos = 0; pos < FRAGMENT_N; pos++) {
            if (tabs[pos] == v) {
                break;
            }
        }
        transaction.commit();
        setTopDrawable(tabs[pos], tabsActiveBackIds[pos]);
    }

    private void setNormalBackgrounds() {
        for (int i = 0; i < tabs.length; i++) {
            Button v = tabs[i];
            setTopDrawable(v, tabsNormalBackIds[i]);
        }
    }

    private void setTopDrawable(Button v, int resId) {
        v.setCompoundDrawablesWithIntrinsicBounds(null, ctx.getResources().getDrawable(resId), null, null);
    }

    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (int i = 0; i < fragmentTags.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(fragmentTags[i]);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            int locType = location.getLocType();
            Logger.d("onReceiveLocation latitude=" + latitude + " longitude=" + longitude
                    + " locType=" + locType + " address=" + location.getAddrStr());
            AVUser user = AVUser.getCurrentUser();
            if (user != null) {
                PreferenceMap preferenceMap = new PreferenceMap(ctx, user.getObjectId());
                AVGeoPoint avGeoPoint = preferenceMap.getLocation();
                if (avGeoPoint != null && avGeoPoint.getLatitude() == location.getLatitude()
                        && avGeoPoint.getLongitude() == location.getLongitude()) {
                    UserService.updateUserLocation();
                    locClient.stop();
                } else {
                    AVGeoPoint newGeoPoint = new AVGeoPoint(location.getLatitude(),
                            location.getLongitude());
                    preferenceMap.setLocation(newGeoPoint);
                }
            }
        }
    }
}
