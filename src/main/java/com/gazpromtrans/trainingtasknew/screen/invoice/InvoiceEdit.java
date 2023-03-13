package com.gazpromtrans.trainingtasknew.screen.invoice;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Invoice;

@UiController("Invoice.edit")
@UiDescriptor("invoice-edit.xml")
@EditedEntityContainer("invoiceDc")
public class InvoiceEdit extends StandardEditor<Invoice> {
    @Subscribe
    public void onInit(InitEvent event) {

    }

}