package com.gazpromtrans.trainingtasknew.screen.servicecompletioncertificate;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.ServiceCompletionCertificate;

@UiController("ServiceCompletionCertificate.browse")
@UiDescriptor("service-completion-certificate-browse.xml")
@LookupComponent("serviceCompletionCertificatesTable")
public class ServiceCompletionCertificateBrowse extends StandardLookup<ServiceCompletionCertificate> {
}