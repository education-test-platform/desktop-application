package com.mdemydovych.nadiya.desktop.broker.resolver.impl;

import com.mdemydovych.nadiya.desktop.broker.resolver.BrokerAware;
import javafx.scene.Scene;

public abstract class BrokerAwareAbstract implements BrokerAware {

  protected boolean isActiveWindow(Scene scene) {
    return scene.getWindow().isShowing();
  }
}
