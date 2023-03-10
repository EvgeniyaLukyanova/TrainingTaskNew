package com.gazpromtrans.trainingtasknew.screen.contract;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Contract;

@UiController("Contract.browse")
@UiDescriptor("contract-browse.xml")
@LookupComponent("contractsTable")
public class ContractBrowse extends StandardLookup<Contract> {
}