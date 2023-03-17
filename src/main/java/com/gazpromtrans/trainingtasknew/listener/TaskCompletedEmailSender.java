package com.gazpromtrans.trainingtasknew.listener;

import com.gazpromtrans.trainingtasknew.entity.Contract;
import com.gazpromtrans.trainingtasknew.entity.User;
import io.jmix.bpm.engine.events.UserTaskCompletedEvent;
import io.jmix.core.DataManager;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.email.EmailInfo;
import io.jmix.email.EmailInfoBuilder;
import io.jmix.email.Emailer;
import org.flowable.engine.RuntimeService;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.flowable.engine.delegate.TaskListener;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class TaskCompletedEmailSender implements TaskListener {
    private static final Logger log = LoggerFactory.getLogger(TaskCompletedEmailSender.class);

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;
    @Autowired
    private Emailer emailer;

    @PostConstruct
    public void onInit() {
        log.info("");
    }


    @EventListener
    public void onTaskAssigned(UserTaskCompletedEvent event) {
        log.info("asd");
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        //List<User> userList = (List<User>) runtimeService.getVariable(delegateTask.getExecutionId(), "userList");
        //delegateTask.getVariables();

        Contract contract = (Contract) delegateTask.getVariable("contract");
        //log.info(userList.toString());
        User user = dataManager.load(User.class)
                .query("select u from User u where u.username = :username")
                .parameter("username", currentAuthentication.getUser().getUsername())
                .one();
        String emailTitle = "Смена статуса";
        String emailBody = "По договору №" + contract.getNumber() + "\n" +
                "был изменен статус на " + contract.getState().getName();
        EmailInfo emailInfo = EmailInfoBuilder.create()
                .setAddresses(user.getEmail())
                .setSubject(emailTitle)
                .setFrom(null)
                .setBody(emailBody)
                .build();
        emailer.sendEmailAsync(emailInfo);
    }
}
