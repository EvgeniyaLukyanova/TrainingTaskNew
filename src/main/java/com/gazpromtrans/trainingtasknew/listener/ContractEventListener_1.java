package com.gazpromtrans.trainingtasknew.listener;

import com.gazpromtrans.trainingtasknew.entity.Contract;
import com.gazpromtrans.trainingtasknew.entity.User;
import io.jmix.core.DataManager;
import io.jmix.core.event.EntityChangedEvent;
import io.jmix.core.security.CurrentAuthentication;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContractEventListener_1 {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @EventListener
    public void onContractChangedBeforeCommit(EntityChangedEvent<Contract> event) {
        if (event.getType() != EntityChangedEvent.Type.DELETED
                && event.getChanges().isChanged("state")) {
            Contract cont = dataManager.load(event.getEntityId()).one();
            if (cont.getState().getName().equals("Новый")) {
                Map<String, Object> params = new HashMap<>();
                params.put("contract", cont);
                List<User> userList = new ArrayList<>(); //(List<User>) params.get("userList");
                User user = dataManager.load(User.class)
                        .query("select u from User u where u.username = :username")
                        .parameter("username", currentAuthentication.getUser().getUsername())
                        .one();
                userList.add(user);
                params.put("userList", userList);
                params.put("manager", user);
                runtimeService.startProcessInstanceByKey(
                        "process",
                        params);
            }
        }
    }
}