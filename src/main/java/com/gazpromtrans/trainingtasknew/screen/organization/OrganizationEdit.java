package com.gazpromtrans.trainingtasknew.screen.organization;

import io.jmix.ui.screen.*;
import com.gazpromtrans.trainingtasknew.entity.Organization;

@UiController("Organization.edit")
@UiDescriptor("organization-edit.xml")
@EditedEntityContainer("organizationDc")
public class OrganizationEdit extends StandardEditor<Organization> {
}