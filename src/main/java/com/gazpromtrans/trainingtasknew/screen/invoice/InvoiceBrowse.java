package com.gazpromtrans.trainingtasknew.screen.invoice;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Invoice;

@UiController("Invoice.browse")
@UiDescriptor("invoice-browse.xml")
@LookupComponent("invoicesTable")
public class InvoiceBrowse extends StandardLookup<Invoice> {
}