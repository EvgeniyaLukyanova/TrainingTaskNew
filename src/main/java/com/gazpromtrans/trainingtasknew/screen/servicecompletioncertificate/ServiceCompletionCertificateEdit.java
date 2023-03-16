package com.gazpromtrans.trainingtasknew.screen.servicecompletioncertificate;

import io.jmix.reportsui.action.list.EditorPrintFormAction;
import io.jmix.ui.Actions;
import io.jmix.ui.component.Button;
import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.ServiceCompletionCertificate;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("ServiceCompletionCertificate.edit")
@UiDescriptor("service-completion-certificate-edit.xml")
@EditedEntityContainer("serviceCompletionCertificateDc")
public class ServiceCompletionCertificateEdit extends StandardEditor<ServiceCompletionCertificate> {
    @Autowired
    private Button print;
    @Autowired
    private Actions actions;

    @Subscribe
    public void onInit(InitEvent event) {
        EditorPrintFormAction action = actions.create(EditorPrintFormAction.class);
        action.setEditor(this);
        action.setReportOutputName(null);
        print.setAction(action);
    }
}