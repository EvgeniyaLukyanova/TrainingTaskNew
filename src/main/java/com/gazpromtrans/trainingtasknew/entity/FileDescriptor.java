package com.gazpromtrans.trainingtasknew.entity;

import io.jmix.core.FileRef;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@JmixEntity
@Table(name = "FILE_DESCRIPTOR", indexes = {
        @Index(name = "IDX_FILE_DESCRIPTOR_CONTRACT", columnList = "CONTRACT_ID"),
        @Index(name = "IDX_FILE_DESCRIPTOR_INVOICE", columnList = "INVOICE_ID")
})
@Entity(name = "file_Descriptor")
public class FileDescriptor {
    @JmixGeneratedValue
    @Column(name = "ID", nullable = false)
    @Id
    private UUID id;

    @NotNull
    @InstanceName
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "FILE_", length = 1024)
    private FileRef file;

    @JoinColumn(name = "CONTRACT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Contract contract;

    @JoinColumn(name = "INVOICE_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public FileRef getFile() {
        return file;
    }

    public void setFile(FileRef file) {
        this.file = file;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}