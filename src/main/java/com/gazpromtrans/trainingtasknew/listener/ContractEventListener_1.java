package com.gazpromtrans.trainingtasknew.listener;

import com.gazpromtrans.trainingtasknew.entity.Contract;
import io.jmix.core.DataManager;
import io.jmix.core.event.EntityChangedEvent;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ContractEventListener_1 {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private DataManager dataManager;

    @EventListener
    public void onContractChangedBeforeCommit(EntityChangedEvent<Contract> event) {
        if (event.getType() != EntityChangedEvent.Type.DELETED
                && event.getChanges().isChanged("state")) {
            Contract cont = dataManager.load(event.getEntityId()).one();
            if (cont.getState().getName().equals("Новый")) {
                Map<String, Object> params = new HashMap<>();
                params.put("contract", cont);
                runtimeService.startProcessInstanceByKey(
                        "process",
                        params);
            }
        }
    }
}