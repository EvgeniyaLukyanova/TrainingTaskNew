package com.gazpromtrans.trainingtasknew.screen.invoice;

import io.jmix.reportsui.action.list.EditorPrintFormAction;
import io.jmix.ui.Actions;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Component;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Invoice;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Invoice.edit")
@UiDescriptor("invoice-edit.xml")
@EditedEntityContainer("invoiceDc")
public class InvoiceEdit extends StandardEditor<Invoice> {
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