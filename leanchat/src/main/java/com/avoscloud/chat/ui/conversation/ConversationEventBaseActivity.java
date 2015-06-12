package com.avoscloud.chat.ui.conversation;

import android.os.Bundle;
import com.avoscloud.chat.service.ConversationChangeEvent;
import com.avoscloud.chat.service.event.FinishEvent;
import com.avoscloud.chat.ui.base_activity.BaseActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by lzw on 15/3/5.
 */
public abstract class ConversationEventBaseActivity extends BaseActivity {
  private EventBus eventBus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    eventBus = EventBus.getDefault();
    eventBus.register(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    eventBus.unregister(this);
  }

  abstract public void onEvent(ConversationChangeEvent conversationChangeEvent);

  public void onEvent(FinishEvent finishEvent) {
    this.finish();
  }
}
