package com.avoscloud.leanchatlib.activity;

import android.util.Log;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;

/**
 * Created by zhangyong on 2015/6/15.
 */
public class CustomMessageHandler extends AVIMMessageHandler {
    @Override
    public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
        // ����Ϣ�����ˡ��������������Լ��Ĵ�����롣
        String msgContent = message.getContent();
        Log.e("CustomMessageHandler", conversation.getConversationId() + " �յ�һ������Ϣ��" + msgContent);
    }
}