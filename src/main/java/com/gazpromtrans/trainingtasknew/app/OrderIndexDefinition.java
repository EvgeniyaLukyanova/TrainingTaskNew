package com.gazpromtrans.trainingtasknew.app;

import com.gazpromtrans.trainingtasknew.entity.Contract;
import io.jmix.search.index.annotation.AutoMappedField;
import io.jmix.search.index.annotation.JmixEntitySearchIndex;
import org.springframework.stereotype.Component;

@JmixEntitySearchIndex(entity = Contract.class)
public interface  OrderIndexDefinition {
    @AutoMappedField(includeProperties =
            {"number", "dateFrom", "dateTo"})
    void orderMapping();
}