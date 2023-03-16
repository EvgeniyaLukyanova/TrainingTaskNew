package com.gazpromtrans.trainingtasknew.listener;

import io.jmix.bpm.engine.events.UserTaskCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TaskListener{
    private static final Logger log = LoggerFactory.getLogger(TaskListener.class);

    @EventListener
    public void onComplete(UserTaskCompletedEvent event) {
      log.info("asd");
    }
}