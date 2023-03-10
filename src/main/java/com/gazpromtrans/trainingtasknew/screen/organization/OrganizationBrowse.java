package com.gazpromtrans.trainingtasknew.screen.organization;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Organization;

@UiController("Organization.browse")
@UiDescriptor("organization-browse.xml")
@LookupComponent("organizationsTable")
public class OrganizationBrowse extends StandardLookup<Organization> {
}