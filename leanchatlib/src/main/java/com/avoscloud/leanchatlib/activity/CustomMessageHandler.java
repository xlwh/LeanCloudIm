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
        // 新消息到来了。在这里增加你自己的处理代码。
        String msgContent = message.getContent();
        Log.e("CustomMessageHandler", conversation.getConversationId() + " 收到一条新消息：" + msgContent);
    }
}