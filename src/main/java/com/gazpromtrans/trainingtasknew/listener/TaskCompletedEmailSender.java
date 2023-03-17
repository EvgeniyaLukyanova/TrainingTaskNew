package com.gazpromtrans.trainingtasknew.listener;

import com.gazpromtrans.trainingtasknew.entity.User;
import io.jmix.bpm.engine.events.UserTaskCompletedEvent;
import org.flowable.engine.RuntimeService;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.flowable.engine.delegate.TaskListener;

import java.util.List;

@Component
public class TaskCompletedEmailSender implements TaskListener {
    private static final Logger log = LoggerFactory.getLogger(TaskCompletedEmailSender.class);

    @Autowired
    private RuntimeService runtimeService;


    @EventListener
    public void onTaskAssigned(UserTaskCompletedEvent event) {
        log.info("asd");
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        List<User> userList = (List<User>) runtimeService.getVariable(delegateTask.getExecutionId(), "userList");
        log.info(userList.toString());
    }
}
