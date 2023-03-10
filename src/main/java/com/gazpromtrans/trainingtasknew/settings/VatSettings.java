package com.gazpromtrans.trainingtasknew.settings;

import io.jmix.appsettings.defaults.AppSettingsDefaultInt;
import io.jmix.appsettings.entity.AppSettingsEntity;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.ui.screen.LookupComponent;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@JmixEntity
@Table(name = "VAT_SETTINGS")
@Entity
public class VatSettings extends AppSettingsEntity {
    @Column(name = "VAT")
    @AppSettingsDefaultInt(20)
    private Integer vat;

    public Integer getVat() {
        return vat;
    }

    public void setVat(Integer vat) {
        this.vat = vat;
    }
}