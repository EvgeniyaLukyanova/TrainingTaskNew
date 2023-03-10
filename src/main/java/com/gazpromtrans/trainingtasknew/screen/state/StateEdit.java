package com.gazpromtrans.trainingtasknew.screen.state;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.State;

@UiController("State.edit")
@UiDescriptor("state-edit.xml")
@EditedEntityContainer("stateDc")
public class StateEdit extends StandardEditor<State> {
}