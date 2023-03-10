package com.gazpromtrans.trainingtasknew.security;

import io.jmix.security.role.annotation.ResourceRole;

@ResourceRole(name = "Initiator", code = "initiator", scope = "UI")
public interface InitiatorRole {
}