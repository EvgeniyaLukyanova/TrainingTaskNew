package com.gazpromtrans.trainingtasknew.screen.stage;

import com.gazpromtrans.trainingtasknew.entity.Contract;
import com.gazpromtrans.trainingtasknew.settings.VatSettings;
import io.jmix.appsettings.AppSettings;
import io.jmix.core.DataManager;
import io.jmix.ui.component.EntityPicker;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Stage;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Stage.edit")
@UiDescriptor("stage-edit.xml")
@EditedEntityContainer("stageDc")
public class StageEdit extends StandardEditor<Stage> {
    @Autowired
    private TextField<Double> vatField;
    @Autowired
    private TextField<Double> totalAmountField;
    @Autowired
    private EntityPicker<Contract> contractField;

    @Autowired
    private AppSettings appSettings;



    @Subscribe("amountField")
    public void onAmountFieldValueChange(HasValue.ValueChangeEvent<Double> event) {
        if (getEditedEntity().getContract().getPerformer() != null) {
            if (getEditedEntity().getContract().getPerformer().getEscapeVat() == true) {
                VatSettings vatSettings = appSettings.load(VatSettings.class);
                getEditedEntity().setVat(event.getValue()*vatSettings.getVat()/100);
                getEditedEntity().setAmount(event.getValue()*(1+vatSettings.getVat()/100));
                //vatField.setValue(event.getValue()*vatSettings.getVat()/100);
                //totalAmountField.setValue(event.getValue()*(1+vatSettings.getVat()/100));
            } else {
                getEditedEntity().setVat(0.0);
                getEditedEntity().setAmount(event.getValue());
                //vatField.setValue(0.0);
                //totalAmountField.setValue(event.getValue());
            }
        } else {
            getEditedEntity().setVat(0.0);
            getEditedEntity().setAmount(event.getValue());
            //vatField.setValue(0.0);
            //totalAmountField.setValue(event.getValue());
        }
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        getEditedEntity();
    }




}