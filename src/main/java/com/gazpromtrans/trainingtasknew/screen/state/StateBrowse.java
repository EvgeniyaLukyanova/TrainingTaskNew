package com.gazpromtrans.trainingtasknew.screen.state;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.State;

@UiController("State.browse")
@UiDescriptor("state-browse.xml")
@LookupComponent("statesTable")
public class StateBrowse extends StandardLookup<State> {
}